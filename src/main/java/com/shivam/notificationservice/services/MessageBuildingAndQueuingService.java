package com.shivam.notificationservice.services;

import com.shivam.notificationservice.Repository.mysql.SmsRepository;
import com.shivam.notificationservice.RequestBody.RawMessageRequestBody;
import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.ResponseBody.ResponseData;
import com.shivam.notificationservice.ResponseBody.ResponseError;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.kafka.MessageIdProducer;
import com.shivam.notificationservice.entity.kafka.MessageIDEntity;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.transformer.RequestTransformer;
import com.shivam.notificationservice.validators.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MessageBuildingAndQueuingService {
    public SmsRepository smsRepository;
    public MessageIdProducer messageIdProducer;
    public GenericResponse<ResponseData,Object,Object> sendToDB(RawMessageRequestBody rawMessageRequestBody)  throws Exception {
        if(!PhoneNumberValidator.isValidPhoneNumber(rawMessageRequestBody.getPhoneNumber())) {
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST, "please check phonenumber"));
        }
        ProcessedMessageEntity processedMessageEntity = RequestTransformer.processRawMessage(rawMessageRequestBody);
        String s = processedMessageEntity.getPhoneNumber();
        processedMessageEntity.setPhoneNumber(s.substring(Math.max(0, s.length() - 10)));
        ProcessedMessageEntity savedVersion = smsRepository.save(processedMessageEntity);
        Long createdId = savedVersion.getId();
        sendToKafka(createdId);
        return new GenericResponse<>(new ResponseData(createdId, "Pending"), null, null);
    }

    public boolean sendToKafka(Long id) throws Exception {
        MessageIDEntity messageIDEntity = new MessageIDEntity(id);
        messageIdProducer.produceMessageId(messageIDEntity);
        return true;
    }

    public GenericResponse<ProcessedMessageEntity, Object, Object> getDetails(Long id) throws BadRequestException {
        if(id < 0){
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST, "provide appropriate request id"));
        }
        Optional<ProcessedMessageEntity> optionalSmsRequestsEntity = smsRepository.findById(id);
        if (optionalSmsRequestsEntity.isPresent()) {
            return new GenericResponse<>(optionalSmsRequestsEntity.get(), null, null);
        } else {
            throw new BadRequestException( new ResponseError(Constants.INVALID_REQUEST, "request_id not found in database"));
        }
    }
}
