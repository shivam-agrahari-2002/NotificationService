package com.shivam.notificationservice.kafka;

import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.kafka.MessageIDEntity;
import com.shivam.notificationservice.services.MessageSendingAndUpdatingService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MessageIdConsumer {
    MessageSendingAndUpdatingService messageSendingAndUpdatingService;
//    KafkaTemplate<String, MessageIDEntity> kafkaTemplate;
    @KafkaListener(topics = Constants.KAFKA_TOPIC, groupId = Constants.KAFKA_GROUP_ID)
    public void consumeMessageId(MessageIDEntity messageIDEntity) throws Exception {
        System.out.println(messageIDEntity);
        messageSendingAndUpdatingService.doFurtherProcessing(messageIDEntity.getId());
    }
}
