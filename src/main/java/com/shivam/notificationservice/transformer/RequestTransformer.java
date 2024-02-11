package com.shivam.notificationservice.transformer;

import com.shivam.notificationservice.requestBody.RawMessageRequestBody;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;

import java.time.LocalDateTime;

public class RequestTransformer {
    public static ProcessedMessageEntity processRawMessage(RawMessageRequestBody rawMessageRequestBody) {
        ProcessedMessageEntity processedMessageEntity = new ProcessedMessageEntity();
        processedMessageEntity.setMessage(rawMessageRequestBody.getMessage());
        processedMessageEntity.setPhoneNumber(rawMessageRequestBody.getPhoneNumber());
        processedMessageEntity.setCreatedAt(LocalDateTime.now());
        return processedMessageEntity;
    }
}
