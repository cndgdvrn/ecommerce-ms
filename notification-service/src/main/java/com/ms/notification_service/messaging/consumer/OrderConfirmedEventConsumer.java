package com.ms.notification_service.messaging.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.contracts.order.OrderConfirmedEventPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.notification_service.application.NotificationApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderConfirmedEventConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationApplicationService notificationApplicationService;

    @KafkaListener(topics = Topics.ORDER_EVENTS, groupId = "notification-service-group")
    public void consume(MessageEnvelope<?> message, Acknowledgment acknowledgment){
        if(message.getMessageType().equals(MessageTypes.ORDER_CONFIRMED_EVENT)){
            OrderConfirmedEventPayload payload = objectMapper.convertValue(message.getPayload(), OrderConfirmedEventPayload.class);
            MessageEnvelope<OrderConfirmedEventPayload> typedMessage = message.withPayload(payload);
            notificationApplicationService.createOrderConfirmedNotification(typedMessage);
            acknowledgment.acknowledge();
            return;
        }
        log.info("Message ignored message type : {}" , message.getMessageType());
        acknowledgment.acknowledge();
    }

}
