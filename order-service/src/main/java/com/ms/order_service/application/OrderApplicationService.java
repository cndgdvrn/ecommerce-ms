package com.ms.order_service.application;


import com.ms.common.contracts.payment.PaymentCompletedEventPayload;
import com.ms.common.contracts.payment.ProcessPaymentCommandPayload;
import com.ms.common.messaging.AggregateTypes;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.order_service.domain.model.Order;
import com.ms.order_service.domain.repository.OrderRepository;
import com.ms.order_service.messaging.publisher.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService {

    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final OrderRepository orderRepository;

    public Long createOrder(){

        Long orderId = System.currentTimeMillis();
        Long customerId = 6L;
        BigDecimal amount = new BigDecimal(750);
        String currency = "TRY";
        UUID correlationId = UUID.randomUUID();

        Order order = new Order(customerId, amount, currency);
        orderRepository.save(order);

        ProcessPaymentCommandPayload payload = new ProcessPaymentCommandPayload(orderId, customerId, amount, currency);
        MessageEnvelope<ProcessPaymentCommandPayload> envelope = MessageEnvelope.of(
                MessageTypes.PROCESS_PAYMENT_COMMAND,
                AggregateTypes.ORDER,
                orderId.toString(),
                correlationId,
                null,
                1,
                payload
        );

        kafkaMessagePublisher.publish(Topics.PAYMENT_COMMANDS,orderId.toString(),envelope);
        log.info("ProcessPaymentCommand published. orderId={}, correlationId={}", orderId, correlationId);
        return orderId;
    }

    @Transactional
    public void markPaymentCompleted(MessageEnvelope<PaymentCompletedEventPayload> event){
        PaymentCompletedEventPayload payload = event.getPayload();

        Order order = orderRepository.findById(payload.orderId())
                .orElseThrow(() -> new IllegalStateException("Order not found. orderId=" + payload.orderId()));

        order.markPaymentCompleted();
        orderRepository.save(order);

        log.info("Order payment completed. orderId={}, paymentId={}, paidAmount={}, correlationId={}, causationId:{}",
                payload.orderId(),
                payload.paymentId(),
                payload.paidAmount(),
                event.getCorrelationId(),
                event.getCausationId()
        );
    }


}
