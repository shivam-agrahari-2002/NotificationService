package com.shivam.notificationservice.kafka;

import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.kafka.MessageIDEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageIdProducer {
    public KafkaTemplate<Long, MessageIDEntity> kafkaTemplate;

    public MessageIdProducer(KafkaTemplate<Long, MessageIDEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean produceMessageId(MessageIDEntity messageIDEntity) throws Exception {
        kafkaTemplate.send(Constants.KAFKA_TOPIC, messageIDEntity);
        return true;
    }
}
