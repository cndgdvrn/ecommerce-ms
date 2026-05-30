package com.ms.payment_service.messaging.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.contracts.payment.ProcessPaymentCommandPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.Topics;
import com.ms.payment_service.application.PaymentApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentCommandConsumer {

    private final ObjectMapper objectMapper;
    private final PaymentApplicationService paymentApplicationService;

    @KafkaListener(
            topics = {Topics.PAYMENT_COMMANDS},
            groupId = "payment-service-group"
    )
    public void consume(MessageEnvelope<?> message, Acknowledgment acknowledgment){
        ProcessPaymentCommandPayload payload = objectMapper.convertValue(message.getPayload(), ProcessPaymentCommandPayload.class);
        MessageEnvelope<ProcessPaymentCommandPayload> typedMessage = new MessageEnvelope<>(
                message.getMessageId(),
                message.getMessageType(),
                message.getAggregateType(),
                message.getAggregateId(),
                message.getCorrelationId(),
                message.getCausationId(),
                message.getVersion(),payload
        );

        paymentApplicationService.processPayment(typedMessage);
        acknowledgment.acknowledge();
    }

}
