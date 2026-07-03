package com.ms.payment_service.application;

import com.ms.common.contracts.payment.*;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.common.util.EcommerceUtil;
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

    public void processPayment(MessageEnvelope<ProcessPaymentCommandPayload> envelope){

        boolean isPaymentSuccess = EcommerceUtil.isPaymentSuccess(100);

        ProcessPaymentCommandPayload payload = envelope.getPayload();
        Long orderId = payload.orderId();
        Long customerId = payload.customerId();
        String paymentId = orderId + "-" + customerId + UUID.randomUUID();
        BigDecimal paidAmount = payload.amount();
        String currency = payload.currency();

        if (!isPaymentSuccess){
            PaymentFailedEventPayload failedPayload = new PaymentFailedEventPayload(payload.orderId(), payload.customerId(), "Payment simulation failed");
            MessageEnvelope<PaymentFailedEventPayload> failedEvent = MessageEnvelope.from(envelope, MessageTypes.PAYMENT_FAILED_EVENT, failedPayload);
            kafkaMessagePublisher.publish(Topics.PAYMENT_EVENTS, orderId.toString(),failedEvent);
            log.info("PaymentFailedEvent published. orderId={}, reason={}",
                    payload.orderId(),
                    failedPayload.reason()
            );
        }else {
            PaymentCompletedEventPayload eventPayload =
                    new PaymentCompletedEventPayload(orderId, customerId, paymentId, paidAmount, currency);
            MessageEnvelope<PaymentCompletedEventPayload> event = MessageEnvelope.from(envelope, MessageTypes.PAYMENT_COMPLETED_EVENT, eventPayload);

            kafkaMessagePublisher.publish(Topics.PAYMENT_EVENTS, orderId.toString(),event);
            log.info("PaymentCompletedEvent published. orderId={}, paymentId={}",
                    payload.orderId(),
                    paymentId
            );
        }
    }


    public void refundPayment(MessageEnvelope<RefundPaymentCommandPayload> command) {
        RefundPaymentCommandPayload payload = command.getPayload();

        String refundId = "refund-" + payload.orderId() + "-" + UUID.randomUUID();

        PaymentRefundedEventPayload eventPayload = new PaymentRefundedEventPayload(
                payload.orderId(),
                payload.customerId(),
                refundId,
                payload.amount(),
                payload.currency(),
                payload.reason()
        );

        MessageEnvelope<PaymentRefundedEventPayload> event = MessageEnvelope.from(
                command,
                MessageTypes.PAYMENT_REFUNDED_EVENT,
                eventPayload
        );

        kafkaMessagePublisher.publish(
                Topics.PAYMENT_EVENTS,
                payload.orderId().toString(),
                event
        );

        log.info("PaymentRefundedEvent published. orderId={}, refundId={}, reason={}",
                payload.orderId(),
                refundId,
                payload.reason()
        );
    }

//    @PostConstruct
//    public void test(){
//        for (int i=0; i<10; i++){
//            System.out.println("*************************************");
//            boolean paymentFailed = PaymentUtil.isPaymentFailed(20);
//            System.out.println(paymentFailed);
//        }
//    }
}
