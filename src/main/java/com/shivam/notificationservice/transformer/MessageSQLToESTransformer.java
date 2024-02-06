package com.shivam.notificationservice.transformer;

import com.shivam.notificationservice.entity.elasticsearch.MessageESEntity;
import com.shivam.notificationservice.entity.mysql.ProcessedMessageEntity;

import java.sql.Timestamp;

public class MessageSQLToESTransformer {
    public static MessageESEntity transform(ProcessedMessageEntity processedMessageEntity){
        MessageESEntity messageESEntity = new MessageESEntity();
        messageESEntity.setId(processedMessageEntity.getId());
        messageESEntity.setMessage(processedMessageEntity.getMessage());
        messageESEntity.setPhoneNumber(processedMessageEntity.getPhoneNumber());
        messageESEntity.setStatus(processedMessageEntity.getStatus());
        messageESEntity.setFailureCode(processedMessageEntity.getFailureCode());
        messageESEntity.setFailureComment(processedMessageEntity.getFailureComment());
        messageESEntity.setCreatedAt(Timestamp.valueOf(processedMessageEntity.getCreatedAt()).getTime());
        messageESEntity.setUpdatedAt(Timestamp.valueOf(processedMessageEntity.getCreatedAt()).getTime());
        return messageESEntity;
    }
}
