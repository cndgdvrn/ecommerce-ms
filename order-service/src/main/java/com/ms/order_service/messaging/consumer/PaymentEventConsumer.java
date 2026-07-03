package com.ms.order_service.messaging.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.contracts.payment.PaymentCompletedEventPayload;
import com.ms.common.contracts.payment.PaymentFailedEventPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
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
    public void consume(MessageEnvelope<?> message, Acknowledgment ack) {
        if (MessageTypes.PAYMENT_COMPLETED_EVENT.equals(message.getMessageType())) {
            PaymentCompletedEventPayload payload = objectMapper.convertValue(message.getPayload(), PaymentCompletedEventPayload.class);
            MessageEnvelope<PaymentCompletedEventPayload> typedMessage = message.withPayload(payload);
            orderApplicationService.markPaymentCompleted(typedMessage);
            ack.acknowledge();
            return;
        }else if (MessageTypes.PAYMENT_FAILED_EVENT.equals(message.getMessageType())) {
            PaymentFailedEventPayload payload = objectMapper.convertValue(message.getPayload(), PaymentFailedEventPayload.class);
            MessageEnvelope<PaymentFailedEventPayload> typedMessage = message.withPayload(payload);
            orderApplicationService.markPaymentFailed(typedMessage);
            ack.acknowledge();
            return;
        }
        log.info("Message ignored. Message Type: {}", message.getMessageType());
        ack.acknowledge();
    }

}
