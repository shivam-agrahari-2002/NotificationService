package com.shivam.notificationservice.services;

import com.shivam.notificationservice.Repository.elasticsearch.MessageESRepository;
import com.shivam.notificationservice.RequestBody.ElasticSearchTextSearchRequestBody;
import com.shivam.notificationservice.RequestBody.ElasticSearchTimeRangeRequestBody;
import com.shivam.notificationservice.RequestBody.PageDetails;
import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.ResponseBody.ResponseError;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.elasticsearch.MessageESEntity;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.exception.BadPageRequestException;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.exception.RepositoryException;
import com.shivam.notificationservice.transformer.MessageSQLToESTransformer;
import com.shivam.notificationservice.utils.ObjectPropertyChecker;
import com.shivam.notificationservice.validators.PageValidator;
import com.shivam.notificationservice.validators.PhoneNumberValidator;
import com.shivam.notificationservice.validators.TimeValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class LogsAndTextSearchService {
    private MessageESRepository messageESRepository;
    public void save(ProcessedMessageEntity processedMessageEntity) throws Exception {
        MessageESEntity messageESEntity = MessageSQLToESTransformer.transform(processedMessageEntity);
        try {
            messageESRepository.save(messageESEntity);
        } catch(Exception e){
            log.debug("ElasticSearch repository : Save() call threw error");
            throw new RepositoryException("ElasticSearch repository : Save() call threw error");
        }
    }
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findByMessage(ElasticSearchTextSearchRequestBody requestBody) throws Exception {
        if(ObjectPropertyChecker.passNullOrEmptyCheck(requestBody)){
            log.error("RequestBody fields are empty | null");
            throw new BadRequestException(new ResponseError("EMPTY_FIELDS","All fields are mandatory"));
        }
        if(PageValidator.checkPageDetails(requestBody.getPageDetails())){
            log.error("page details didn't pass the validation");
            throw new BadPageRequestException("Page index Should Be Non-Negative & Size should have range(1-50)");
        }
        PageRequest pageRequest = PageRequest.of(requestBody.getPageDetails().getPage(),requestBody.getPageDetails().getSize());
        try {
            Page<MessageESEntity> result = messageESRepository.findByMessageContaining(requestBody.getText(), pageRequest);
            return new GenericResponse<>(result.getContent(), null, new PageDetails(result.getNumber(), result.getSize()));
        } catch(Exception e){
            log.debug("ElasticSearch repository : findByMessage() call threw error");
            throw new RepositoryException("ElasticSearch repository : findByMessage() call threw error");
        }
    }
    public GenericResponse<List<MessageESEntity>,String,PageDetails> findByPhoneNumberAndTimeRange(ElasticSearchTimeRangeRequestBody requestBody) throws Exception {
        if(ObjectPropertyChecker.passNullOrEmptyCheck(requestBody)){
            log.error("RequestBody fields are empty | null");
            throw new BadRequestException(new ResponseError("EMPTY_FIELDS","All fields are mandatory"));
        }
        if(PageValidator.checkPageDetails(requestBody.getPageDetails())){
            log.error("page details didn't pass the validation");
            throw new BadPageRequestException("Page index Should Be Non-Negative & Size should have range(1-50)");
        }
        if(!TimeValidator.checkTimings(requestBody)){
            log.error("Requested time range is invalid");
            throw new BadRequestException(new ResponseError("INVALID_TIME_RANGE","start time should be less than end time"));
        }
        if(PhoneNumberValidator.isValidPhoneNumber(requestBody.getPhoneNumber())){
            log.error("phoneNumber didn't pass validation");
            throw new BadRequestException(new ResponseError(Constants.INVALID_REQUEST,"check your phoneNumber"));
        }
//        requestBody.setPhoneNumber(requestBody.getPhoneNumber().substring(Math.max(0, s.length() - 10)));
        Long startTime = Timestamp.valueOf(requestBody.getStartTime()).getTime();
        Long endTime = Timestamp.valueOf(requestBody.getEndTime()).getTime();
        PageRequest pageRequest = PageRequest.of(requestBody.getPageDetails().getPage(),requestBody.getPageDetails().getSize());
        try {
            Page<MessageESEntity> result = messageESRepository.findByPhoneNumberAndCreatedAtBetween(requestBody.getPhoneNumber(), startTime, endTime, pageRequest);
            return new GenericResponse<>(result.getContent(), null, new PageDetails(result.getNumber(), result.getSize()));
        } catch(Exception e){
            log.debug("ElasticSearch repository : findByPhoneNumberAndCreatedAtBetween() threw error");
            throw new RepositoryException("ElasticSearch repository : findByPhoneNumberAndCreatedAtBetween() threw error");
        }
    }

    public GenericResponse<List<MessageESEntity>,String,PageDetails> findAll(PageDetails pageDetails) throws Exception {
        if(PageValidator.checkPageDetails(pageDetails)){
            log.error("page details didn't pass the validation");
            throw new BadPageRequestException("Page index Should Be Non-Negative & Size should have range(1-50)");
        }
        PageRequest pageRequest = PageRequest.of(pageDetails.getPage(), pageDetails.getSize());
        try {
            Page<MessageESEntity> result = messageESRepository.findAll(pageRequest);
            return new GenericResponse<>((List<MessageESEntity>) result.getContent(), null, new PageDetails(result.getNumber(), result.getSize()));
        } catch(Exception e){
            log.debug("ElasticSearch repository : findAll() threw error");
            throw new RepositoryException("ElasticSearch repository : findAll() threw error");
        }
    }
}
