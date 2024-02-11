package com.shivam.notificationservice.servicetest;
import com.shivam.notificationservice.repository.mysql.SmsRepository;
import com.shivam.notificationservice.responseBody.external.ImiconnectMessagingResponse;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.services.BlackListService;
import com.shivam.notificationservice.services.LogsAndTextSearchService;
import com.shivam.notificationservice.services.MessageSendingAndUpdatingService;
import com.shivam.notificationservice.services.external.ThirdPartyMessagingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"com.shivam.notificationservice"})
class MessageSendingAndUpdatingServiceTest {

    @Mock
    private SmsRepository smsRepository;

    @Mock
    private BlackListService blackListService;

    @Mock
    private ThirdPartyMessagingService thirdPartyMessagingService;

    @Mock
    private LogsAndTextSearchService logsAndTextSearchService;

    @InjectMocks
    private MessageSendingAndUpdatingService messageSendingAndUpdatingService;

    @Test
    void testGetMessage() throws Exception {
        // Arrange
        Long id = 1L;
        ProcessedMessageEntity processedMessageEntity = new ProcessedMessageEntity();
        when(smsRepository.findById(id)).thenReturn(Optional.of(processedMessageEntity));

        // Act
        ProcessedMessageEntity result = messageSendingAndUpdatingService.getMessage(id);

        // Assert
        assertThat(result).isSameAs(processedMessageEntity);
    }

    @Test
    void testCheckBlackList() throws Exception {
        String phoneNumber = "1234567890";
        when(blackListService.blackListChecker(phoneNumber)).thenReturn(false).thenReturn(true);

        boolean result = messageSendingAndUpdatingService.checkBlackList(phoneNumber);
        assertThat(result).isFalse();

        result = messageSendingAndUpdatingService.checkBlackList(phoneNumber);
        assertThat(result).isTrue();
    }

    @Test
    void testDoFurtherProcessing_BlackListed() throws Exception {
        // Arrange
        Long id = 1L;
        ProcessedMessageEntity processedMessageEntity = new ProcessedMessageEntity();
        processedMessageEntity.setId(id);
        processedMessageEntity.setPhoneNumber("1234567890");
        when(smsRepository.findById(id)).thenReturn(Optional.of(processedMessageEntity));
        when(blackListService.blackListChecker(anyString())).thenReturn(true);

        // Act
        messageSendingAndUpdatingService.doFurtherProcessing(id);

        // Assert
        assertThat(processedMessageEntity.getStatus()).isEqualTo("message blocked");
        assertThat(processedMessageEntity.getFailureCode()).isEqualTo("Blacklisted phonenumber");
        assertThat(processedMessageEntity.getFailureComment()).isEqualTo("can't send message to a blacklisted phoneNumber");
        // Add more assertions as needed
    }

    @Test
    void testDoFurtherProcessing_NotBlackListed() throws Exception {
        // Arrange
        Long id = 1L;
        ProcessedMessageEntity processedMessageEntity = new ProcessedMessageEntity();
        processedMessageEntity.setId(id);
        processedMessageEntity.setPhoneNumber("1234567890");
        processedMessageEntity.setMessage("test message");
        when(smsRepository.findById(id)).thenReturn(Optional.of(processedMessageEntity));
        when(blackListService.blackListChecker(anyString())).thenReturn(false);
        ResponseEntity<ImiconnectMessagingResponse> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(thirdPartyMessagingService.sendMessage(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(responseEntity);

        // Act
        messageSendingAndUpdatingService.doFurtherProcessing(id);

        // Assert
        assertThat(processedMessageEntity.getStatus()).isEqualTo(HttpStatus.OK.toString());
        // Add more assertions as needed
    }
}

