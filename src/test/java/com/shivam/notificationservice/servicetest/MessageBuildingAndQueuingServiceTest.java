package com.shivam.notificationservice.servicetest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.shivam.notificationservice.Repository.mysql.SmsRepository;
import com.shivam.notificationservice.RequestBody.RawMessageRequestBody;
import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.ResponseBody.ResponseData;
import com.shivam.notificationservice.ResponseBody.ResponseError;
import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.kafka.MessageIDEntity;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.exception.RepositoryException;
import com.shivam.notificationservice.kafka.MessageIdProducer;
import com.shivam.notificationservice.services.MessageBuildingAndQueuingService;
import com.shivam.notificationservice.transformer.RequestTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;

@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = {"com.shivam.notificationservice"})
class MessageBuildingAndQueuingServiceTest {

    @Mock
    private SmsRepository smsRepository;

    @Mock
    private MessageIdProducer messageIdProducer;

    @InjectMocks
    private MessageBuildingAndQueuingService messageService;

    @BeforeEach
    void setUp() {
        reset(smsRepository, messageIdProducer);
    }

    @Test
    public void testSendToDB_invalidPhoneNumber() throws Exception {
        RawMessageRequestBody rawMessageRequestBody = new RawMessageRequestBody("123456789","test message");
        BadRequestException exception = assertThrows(BadRequestException.class,()->messageService.sendToDB(rawMessageRequestBody));
        verify(smsRepository,never()).save(any(ProcessedMessageEntity.class));
        verify(messageIdProducer,never()).produceMessageId(any(MessageIDEntity.class));
        Assertions.assertThat(exception.getResponseError().getCode()).isEqualTo(Constants.INVALID_REQUEST);
        Assertions.assertThat(exception.getResponseError().getMessage()).isEqualTo("please check phonenumber");
    }
    @Test
    public void testSendToDB_validPhoneNumber() throws Exception {
        RawMessageRequestBody rawMessageRequestBody = new RawMessageRequestBody("1234567890","test message");
        ProcessedMessageEntity processedMessageEntity = RequestTransformer.processRawMessage(rawMessageRequestBody);
        Long id = 0L;
        processedMessageEntity.setId(id);
        when(smsRepository.save(any(ProcessedMessageEntity.class))).thenReturn(processedMessageEntity);
        when(messageIdProducer.produceMessageId(new MessageIDEntity(id))).thenReturn(true);
        GenericResponse<ResponseData,Object,Object> response = messageService.sendToDB(rawMessageRequestBody);
        verify(smsRepository,times(1)).save(any(ProcessedMessageEntity.class));
        verify(messageIdProducer,times(1)).produceMessageId(any(MessageIDEntity.class));
        Assertions.assertThat(response.getData()).isNotNull();
        Assertions.assertThat(response.getData().getRequestId()).isEqualTo(0);
        Assertions.assertThat(response.getData().getComments()).isEqualTo("Pending");
    }
    @Test
    void testSendToKafka() throws Exception {
        Long messageId = 123L;
        when(messageIdProducer.produceMessageId(new MessageIDEntity(messageId))).thenReturn(true);
        boolean check = messageService.sendToKafka(messageId);
        Assertions.assertThat(check).isTrue();
    }

    @Test
    void testGetDetails_negativeId(){
        Long id = -1L;
        BadRequestException exception = assertThrows(BadRequestException.class,()->messageService.getDetails(id));
        verify(smsRepository,never()).findById(id);
        Assertions.assertThat(exception.getResponseError().getCode()).isEqualTo(Constants.INVALID_REQUEST);
        Assertions.assertThat(exception.getResponseError().getMessage()).isEqualTo("provide appropriate request id");
    }
    @Test
    void testGetDetails_idNotPresentInDb(){
        when(smsRepository.findById(anyLong())).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class,()->messageService.getDetails(anyLong()));
        verify(smsRepository,times(1)).findById(anyLong());
        Assertions.assertThat(exception.getResponseError().getCode()).isEqualTo(Constants.INVALID_REQUEST);
        Assertions.assertThat(exception.getResponseError().getMessage()).isEqualTo("request_id not found in database");
    }
    @Test
    void testGetDetails_idPresent() throws BadRequestException, RepositoryException {
        Long id = 0L;
        ProcessedMessageEntity processedMessageEntity = new ProcessedMessageEntity();
        processedMessageEntity.setId(id);
        when(smsRepository.findById(id)).thenReturn(Optional.of(processedMessageEntity));
        GenericResponse<ProcessedMessageEntity, Object, Object> response = messageService.getDetails(anyLong());
        verify(smsRepository,times(1)).findById(anyLong());
        Assertions.assertThat(response.getData().getId()).isEqualTo(id);
        Assertions.assertThat(response.getData()).isNotNull();
    }
}


