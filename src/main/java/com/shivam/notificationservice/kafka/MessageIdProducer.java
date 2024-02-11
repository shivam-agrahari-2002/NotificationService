package com.shivam.notificationservice.kafka;

import com.shivam.notificationservice.constants.Constants;
import com.shivam.notificationservice.entity.kafka.MessageIDEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MessageIdProducer {

    @Autowired
    public KafkaTemplate<String, MessageIDEntity> kafkaTemplate;

    public boolean produceMessageId(MessageIDEntity messageIDEntity) throws Exception {
        kafkaTemplate.send(Constants.KAFKA_TOPIC, messageIDEntity);
        return true;
    }
}
