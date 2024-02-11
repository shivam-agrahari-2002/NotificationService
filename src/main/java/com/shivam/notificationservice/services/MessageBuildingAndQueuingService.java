package com.shivam.notificationservice.services;

import com.shivam.notificationservice.repository.mysql.SmsRepository;
import com.shivam.notificationservice.requestBody.RawMessageRequestBody;
import com.shivam.notificationservice.responseBody.GenericResponse;
import com.shivam.notificationservice.responseBody.ResponseData;
import com.shivam.notificationservice.responseBody.ResponseError;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.exception.RepositoryException;
import com.shivam.notificationservice.kafka.MessageIdProducer;
import com.shivam.notificationservice.entity.kafka.MessageIDEntity;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.transformer.RequestTransformer;
import com.shivam.notificationservice.validators.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MessageBuildingAndQueuingService {
    public SmsRepository smsRepository;
    public MessageIdProducer messageIdProducer;
    public GenericResponse<ResponseData,Object,Object> sendToDB(RawMessageRequestBody rawMessageRequestBody)  throws Exception {
        if(PhoneNumberValidator.isValidPhoneNumber(rawMessageRequestBody.getPhoneNumber())) {
            log.error("wrong input format : phonenumber validation failed");
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST, "please check phonenumber"));
        }
        ProcessedMessageEntity processedMessageEntity = RequestTransformer.processRawMessage(rawMessageRequestBody);
        String s = processedMessageEntity.getPhoneNumber();
        processedMessageEntity.setPhoneNumber(s.substring(Math.max(0, s.length() - 10)));

        ProcessedMessageEntity savedVersion = null;
        try {
            savedVersion = smsRepository.save(processedMessageEntity);
        } catch(Exception e){
            log.debug("smsRepository gave error while creating new record: check mysql database errors");
            throw new RepositoryException("smsRepository gave error while creating new record: check mysql database errors");
        }
        Long createdId = savedVersion.getId();
        sendToKafka(createdId);
        return new GenericResponse<>(new ResponseData(createdId, "Pending"), null, null);
    }

    public boolean sendToKafka(Long id) throws Exception {
        MessageIDEntity messageIDEntity = new MessageIDEntity(id);
        try {
            messageIdProducer.produceMessageId(messageIDEntity);
        } catch (Exception e){
            log.debug("messageIdProducer cant produce messageId: check for kafka");
            throw new RepositoryException("messageIdProducer cant produce messageId: check for kafka");
        }
        return true;
    }

    public GenericResponse<ProcessedMessageEntity, Object, Object> getDetails(Long id) throws BadRequestException, RepositoryException {
        if(id < 0){
            log.error("wrong request id " + id + ": provided id is negative --- id should be > 0");
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST, "provide appropriate request id"));
        }
        Optional<ProcessedMessageEntity> optionalSmsRequestsEntity = Optional.empty();
        try {
            optionalSmsRequestsEntity = smsRepository.findById(id);
        } catch(Exception e){
            log.debug("smsRepository gave error while searching for id " + id + ": check mysql database errors");
            throw new RepositoryException("smsRepository gave error while searching for id " + id + ": check mysql database errors");
        }
        if (optionalSmsRequestsEntity.isPresent()) {
            return new GenericResponse<>(optionalSmsRequestsEntity.get(), null, null);
        } else {
            log.error("wrong request id " + id + ": provided id is negative --- id should be > 0");
            throw new BadRequestException( new ResponseError(Constants.INVALID_REQUEST, "request_id not found in database"));
        }
    }
}
