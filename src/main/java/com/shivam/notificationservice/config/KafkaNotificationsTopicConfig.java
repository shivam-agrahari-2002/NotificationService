package com.shivam.notificationservice.config;

import com.shivam.notificationservice.constants.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaNotificationsTopicConfig {
    public NewTopic notificationTopic(){
        return TopicBuilder.name(Constants.KAFKA_TOPIC).build();
    }
}
