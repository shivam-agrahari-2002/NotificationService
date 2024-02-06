package com.shivam.notificationservice.services;

import com.shivam.notificationservice.Repository.mysql.SmsRepository;
import com.shivam.notificationservice.ResponseBody.external.ImiconnectMessagingResponse;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.services.external.ThirdPartyMessagingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MessageSendingAndUpdatingService {
    SmsRepository smsRepository;
    BlackListService blackListService;
    ThirdPartyMessagingService thirdPartyMessagingService;
    LogsAndTextSearchService logsAndTextSearchService;
    public ProcessedMessageEntity getMessage(Long id) throws Exception {
            Optional<ProcessedMessageEntity> optionalSmsRequestsEntity = smsRepository.findById(id);
            return optionalSmsRequestsEntity.orElse(null);
    }
    public boolean checkBlackList(String phoneNumber)  throws Exception {
        return blackListService.blackListChecker(phoneNumber);
    }
    public void doFurtherProcessing(Long id) throws Exception {
        ProcessedMessageEntity processedMessageEntity = getMessage(id);
        if(checkBlackList(processedMessageEntity.getPhoneNumber())){
            processedMessageEntity.setStatus("message blocked");
            processedMessageEntity.setFailureCode("Blacklisted phonenumber");
            processedMessageEntity.setFailureComment("can't send message to a blacklisted phoneNumber");

        } else {
            ResponseEntity<ImiconnectMessagingResponse> response = thirdPartyMessagingService.sendMessage(Constants.API_KEY, processedMessageEntity.getPhoneNumber(),(processedMessageEntity.getId()).toString(),Constants.API_URL, processedMessageEntity.getMessage());
            processedMessageEntity.setStatus(response.getStatusCode().toString());
            logsAndTextSearchService.save(processedMessageEntity);
        }
        smsRepository.save(processedMessageEntity);
    }
}
