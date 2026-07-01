package com.ms.payment_service.application;

import com.ms.common.contracts.payment.PaymentCompletedEventPayload;
import com.ms.common.contracts.payment.ProcessPaymentCommandPayload;
import com.ms.common.messaging.AggregateTypes;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.payment_service.messaging.publisher.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentApplicationService {

    private final KafkaMessagePublisher kafkaMessagePublisher;

    public void processPayment(MessageEnvelope<ProcessPaymentCommandPayload> command){
        ProcessPaymentCommandPayload payload = command.getPayload();

        Long orderId = payload.orderId();
        Long customerId = payload.customerId();
        String paymentId = orderId + "-" + customerId + UUID.randomUUID();
        BigDecimal paidAmount = payload.amount();
        String currency = payload.currency();


        PaymentCompletedEventPayload eventPayload =
                new PaymentCompletedEventPayload(orderId, customerId, paymentId, paidAmount, currency);


        MessageEnvelope<ProcessPaymentCommandPayload> event = MessageEnvelope.from(command, MessageTypes.PAYMENT_COMPLETED_EVENT, command.getPayload());

        kafkaMessagePublisher.publish(Topics.PAYMENT_EVENTS, orderId.toString(),event);

        log.info("PaymentCompletedEvent published. orderId={}, paymentId={}",
                payload.orderId(),
                paymentId
        );
    }

}
