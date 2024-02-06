package com.shivam.notificationservice.servicetest;

import com.shivam.notificationservice.Repository.elasticsearch.MessageESRepository;
import com.shivam.notificationservice.RequestBody.ElasticSearchTextSearchRequestBody;
import com.shivam.notificationservice.RequestBody.ElasticSearchTimeRangeRequestBody;
import com.shivam.notificationservice.RequestBody.PageDetails;
import com.shivam.notificationservice.ResponseBody.GenericResponse;
import com.shivam.notificationservice.entity.elasticsearch.MessageESEntity;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;
import com.shivam.notificationservice.exception.BadRequestException;
import com.shivam.notificationservice.services.LogsAndTextSearchService;
import com.shivam.notificationservice.transformer.MessageSQLToESTransformer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.shivam.notificationservice")
class LogsAndTextSearchServiceTest {

    @Mock
    private MessageESRepository messageESRepository;

    @InjectMocks
    private LogsAndTextSearchService logsAndTextSearchService;

    @BeforeEach
    void setUp() {
        reset(messageESRepository);
    }

    @Test
    void testSave() throws Exception {
        // Arrange
        ProcessedMessageEntity processedMessageEntity = new ProcessedMessageEntity("1234567890", "Test message");
        MessageESEntity messageESEntity = new MessageESEntity(); // Create a dummy MessageESEntity
        when(messageESRepository.save(any(MessageESEntity.class))).thenReturn(messageESEntity);
        logsAndTextSearchService.save(processedMessageEntity);
        verify(messageESRepository,times(1)).save(any(MessageESEntity.class));
    }

    @Test
    void testFindByMessage_normalResults() throws Exception {
        PageDetails pageDetails = new PageDetails(0, 10);
        ElasticSearchTextSearchRequestBody requestBody = new ElasticSearchTextSearchRequestBody("message", pageDetails); // Create a dummy requestBody
        Page<MessageESEntity> dummyPage = createDummyPage(false);
        when(messageESRepository.findByMessageContaining(anyString(), any(PageRequest.class))).thenReturn(dummyPage);
        GenericResponse<List<MessageESEntity>, String, PageDetails> response = logsAndTextSearchService.findByMessage(requestBody);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getData().size()).isGreaterThan(0);
        Assertions.assertThat(response.getPageDetails().getPage()).isEqualTo(0);
        Assertions.assertThat(response.getPageDetails().getSize()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void testFindByPhoneNumberAndTimeRange_invalidTimeRange() throws Exception {
        ElasticSearchTimeRangeRequestBody requestBody = new ElasticSearchTimeRangeRequestBody(null,LocalDateTime.parse("2024-01-29T16:02:28"),LocalDateTime.parse("2024-01-29T16:02:20"),new PageDetails(0,10));
        BadRequestException exception = assertThrows(BadRequestException.class,()->logsAndTextSearchService.findByPhoneNumberAndTimeRange(requestBody));
        verify(messageESRepository,never()).findByPhoneNumberAndCreatedAtBetween(anyString(),anyLong(),anyLong(),any(PageRequest.class));
        Assertions.assertThat(exception.getResponseError().getCode()).isEqualTo("INVALID_TIME_RANGE");
        Assertions.assertThat(exception.getResponseError().getMessage()).isEqualTo("start time should be less than end time");
    }

    @Test
    void testFindByPhoneNumberAndTimeRange_normalResults() throws Exception {
        // Arrange
        PageDetails pageDetails = new PageDetails(0, 10);
        ElasticSearchTimeRangeRequestBody requestBody = new ElasticSearchTimeRangeRequestBody("1234567890", LocalDateTime.now(),LocalDateTime.now(),pageDetails);
        Page<MessageESEntity> dummyPage = createDummyPage(false);
        when(messageESRepository.findByPhoneNumberAndCreatedAtBetween(anyString(), anyLong(), anyLong(), any(PageRequest.class))).thenReturn(dummyPage);

        // Act
        GenericResponse<List<MessageESEntity>, String, PageDetails> response = logsAndTextSearchService.findByPhoneNumberAndTimeRange(requestBody);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getData().size()).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(response.getPageDetails().getPage()).isEqualTo(0);
        Assertions.assertThat(response.getPageDetails().getSize()).isGreaterThanOrEqualTo(0);
    }
    @Test
    void testFindAll() throws Exception {
        // Arrange
        PageDetails pageDetails = new PageDetails(0, 10); // Create dummy page details
        Page<MessageESEntity> dummyPage = createDummyPage(false); // Create a dummy page
        when(messageESRepository.findAll(any(PageRequest.class))).thenReturn(dummyPage);

        // Act
        GenericResponse<List<MessageESEntity>, String, PageDetails> response = logsAndTextSearchService.findAll(pageDetails);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getData().size()).isGreaterThan(0);
        Assertions.assertThat(response.getPageDetails().getPage()).isEqualTo(0);
        Assertions.assertThat(response.getPageDetails().getSize()).isGreaterThanOrEqualTo(0);
        // Add more assertions as needed
    }

    private Page<MessageESEntity> createDummyPage(boolean empty) {
        List<MessageESEntity> content = new ArrayList<>();
        if(!empty) {
            content.add(new MessageESEntity()); // Add a dummy MessageESEntity to the list
        }
        return new PageImpl<>(content);
    }
}
