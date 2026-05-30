package com.ms.order_service.messaging.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.contracts.payment.PaymentCompletedEventPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.Topics;
import com.ms.order_service.application.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final OrderApplicationService orderApplicationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = {Topics.PAYMENT_EVENTS}, groupId = "order-service-group"
    )
    public void consume(MessageEnvelope<?> message, Acknowledgment ack){

        log.info("Message received from payment.events. messageId={}, type={}",
                message.getMessageId(),
                message.getMessageType()
        );
        if(!"PaymentCompletedEvent".equals(message.getMessageType())){
            log.info("Message ignored. messageType={}", message.getMessageType());
            ack.acknowledge();
            return;
        }
        PaymentCompletedEventPayload paylaod = objectMapper.convertValue(message.getPayload(), PaymentCompletedEventPayload.class);
        orderApplicationService.markPaymentCompleted(paylaod);
        ack.acknowledge();
    }

}
