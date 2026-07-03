package com.ms.payment_service.messaging.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.contracts.payment.ProcessPaymentCommandPayload;
import com.ms.common.contracts.payment.RefundPaymentCommandPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
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

        if (MessageTypes.REFUND_PAYMENT_COMMAND.equals(message.getMessageType())){
            RefundPaymentCommandPayload payload = objectMapper.convertValue(message.getPayload(), RefundPaymentCommandPayload.class);
            MessageEnvelope<RefundPaymentCommandPayload> typedMessage = message.withPayload(payload);
            paymentApplicationService.refundPayment(typedMessage);
            acknowledgment.acknowledge();
            return;
        }else if (MessageTypes.PROCESS_PAYMENT_COMMAND.equals(message.getMessageType())){
            ProcessPaymentCommandPayload payload = objectMapper.convertValue(message.getPayload(), ProcessPaymentCommandPayload.class);
            MessageEnvelope<ProcessPaymentCommandPayload> typedMessage = message.withPayload(payload);
            paymentApplicationService.processPayment(typedMessage);
            acknowledgment.acknowledge();
            return;
        }

        log.info("Message ignored. messageType={}", message.getMessageType());
        acknowledgment.acknowledge();
     }

}
