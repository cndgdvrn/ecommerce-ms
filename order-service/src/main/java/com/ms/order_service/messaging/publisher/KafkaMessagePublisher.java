package com.ms.order_service.messaging.publisher;


import com.ms.common.messaging.MessageEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaMessagePublisher {

    private final KafkaTemplate<String, MessageEnvelope<?>> kafkaTemplate;

    public void publish(String topic, String key, MessageEnvelope<?> message) {
        kafkaTemplate.send(topic,key,message);
    }

}
