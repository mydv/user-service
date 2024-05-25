package com.mohity.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    private static final String TOPIC = "user-events";

    public void publishUserEvent(UserEvent event) {
        CompletableFuture<SendResult<String, UserEvent>> future = kafkaTemplate.send(TOPIC, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message to Kafka: " + result.getRecordMetadata());
            } else {
                System.err.println("Failed to send message to Kafka: " + ex.getMessage());
            }
        });
    }
}
