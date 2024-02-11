package com.shivam.notificationservice.services;

import com.shivam.notificationservice.repository.mysql.SmsRepository;
import com.shivam.notificationservice.responseBody.external.ImiconnectMessagingResponse;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.exception.RepositoryException;
import com.shivam.notificationservice.services.external.ThirdPartyMessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MessageSendingAndUpdatingService {
    SmsRepository smsRepository;
    BlackListService blackListService;
    ThirdPartyMessagingService thirdPartyMessagingService;
    LogsAndTextSearchService logsAndTextSearchService;
    public ProcessedMessageEntity getMessage(Long id) throws Exception {
        Optional<ProcessedMessageEntity> optionalSmsRequestsEntity = smsRepository.findById(id);
        try {
            return optionalSmsRequestsEntity.orElse(null);
        } catch (Exception e){
            log.debug("mysql threw error : findById()");
            throw new RepositoryException("mysql threw error : findById()");
        }
    }
    public boolean checkBlackList(String phoneNumber)  throws Exception {
        return blackListService.blackListChecker(phoneNumber);
    }
    public void doFurtherProcessing(Long id) throws Exception {
        ProcessedMessageEntity processedMessageEntity = getMessage(id);
        if(checkBlackList(processedMessageEntity.getPhoneNumber())){
            log.error("given Number is Blacklisted");
            processedMessageEntity.setStatus("message blocked");
            processedMessageEntity.setFailureCode("Blacklisted phonenumber");
            processedMessageEntity.setFailureComment("can't send message to a blacklisted phoneNumber");

        } else {
            ResponseEntity<ImiconnectMessagingResponse> response = thirdPartyMessagingService.sendMessage(Constants.API_KEY, processedMessageEntity.getPhoneNumber(),(processedMessageEntity.getId()).toString(),Constants.API_URL, processedMessageEntity.getMessage());
            processedMessageEntity.setStatus(response.getStatusCode().toString());
            logsAndTextSearchService.save(processedMessageEntity);
        }
        try {
            processedMessageEntity.setUpdatedAt(LocalDateTime.now());
            smsRepository.save(processedMessageEntity);
        } catch(Exception e){
            log.debug("smsRepository gave error while creating new record: check mysql database errors");
            throw new RepositoryException("smsRepository gave error while creating new record: check mysql database errors");
        }
    }
}
