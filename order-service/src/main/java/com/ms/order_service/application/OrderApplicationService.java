package com.ms.order_service.application;


import com.ms.common.contracts.inventory.OrderItemPayload;
import com.ms.common.contracts.inventory.ReserveStockCommandPayload;
import com.ms.common.contracts.inventory.StockReservationFailedEventPayload;
import com.ms.common.contracts.inventory.StockReservedEventPayload;
import com.ms.common.contracts.order.OrderConfirmedEventPayload;
import com.ms.common.contracts.payment.PaymentCompletedEventPayload;
import com.ms.common.contracts.payment.PaymentFailedEventPayload;
import com.ms.common.contracts.payment.ProcessPaymentCommandPayload;
import com.ms.common.messaging.AggregateTypes;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.order_service.api.dto.OrderResponse;
import com.ms.order_service.domain.model.Order;
import com.ms.order_service.domain.repository.OrderRepository;
import com.ms.order_service.messaging.publisher.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService {

    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final OrderRepository orderRepository;

    @Transactional
    public Long createOrder(){

        Long customerId = 6L;
        BigDecimal amount = new BigDecimal(750);
        String currency = "TRY";
        UUID correlationId = UUID.randomUUID();

        Order order = new Order(customerId, amount, currency);
        orderRepository.save(order);

        ProcessPaymentCommandPayload payload = new ProcessPaymentCommandPayload(order.getId(), customerId, amount, currency);
        MessageEnvelope<ProcessPaymentCommandPayload> envelope = MessageEnvelope.of(
                MessageTypes.PROCESS_PAYMENT_COMMAND,
                AggregateTypes.ORDER,
                order.getId().toString(),
                correlationId,
                null,
                1,
                payload
        );

        kafkaMessagePublisher.publish(Topics.PAYMENT_COMMANDS,order.getId().toString(),envelope);
        log.info("ProcessPaymentCommand published. orderId={}, correlationId={}", order.getId(), correlationId);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found. orderId=" + orderId));

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    @Transactional
    public void markPaymentCompleted(MessageEnvelope<PaymentCompletedEventPayload> event){
        PaymentCompletedEventPayload payload = event.getPayload();

        Order order = orderRepository.findById(payload.orderId())
                .orElseThrow(() -> new IllegalStateException("Order not found. orderId=" + payload.orderId()));

        order.markPaymentCompleted();
        order.markStockPending();
        orderRepository.save(order);

        ReserveStockCommandPayload reserveStockPayload = new ReserveStockCommandPayload(payload.orderId(), List.of(new OrderItemPayload(1L, 1)));
        MessageEnvelope<ReserveStockCommandPayload> reserveStockCommand = MessageEnvelope.from(event, MessageTypes.RESERVE_STOCK_COMMAND, reserveStockPayload);
        kafkaMessagePublisher.publish(Topics.INVENTORY_COMMANDS, payload.orderId().toString(),reserveStockCommand);

        log.info("Order payment completed. orderId={}, paymentId={}, paidAmount={}, correlationId={}, causationId={}",
                payload.orderId(),
                payload.paymentId(),
                payload.paidAmount(),
                event.getCorrelationId(),
                event.getCausationId()
        );
    }

    @Transactional
    public void markStockReserved(MessageEnvelope<StockReservedEventPayload> event){
        StockReservedEventPayload payload = event.getPayload();

        Order order = orderRepository.findById(payload.orderId()).orElseThrow(() -> new IllegalStateException("Order not found. orderId: " + payload.orderId()));
        order.markStockReserved();
        order.markConfirmed();
        orderRepository.save(order);

        OrderConfirmedEventPayload eventPayload = new OrderConfirmedEventPayload(payload.orderId(), order.getCustomerId(), order.getTotalAmount(), order.getCurrency());
        MessageEnvelope<OrderConfirmedEventPayload> orderConfirmedEvent = MessageEnvelope.from(event, MessageTypes.ORDER_CONFIRMED_EVENT, eventPayload);
        kafkaMessagePublisher.publish(Topics.ORDER_EVENTS,order.getId().toString(),orderConfirmedEvent);

        log.info("Order confirmed. orderId={}, correlationId={}, causationId={}",
                payload.orderId(),
                event.getCorrelationId(),
                event.getCausationId()
        );
    }

    @Transactional
    public void markStockFailed(MessageEnvelope<StockReservationFailedEventPayload> event){
        StockReservationFailedEventPayload payload = event.getPayload();
        Order order = orderRepository.findById(payload.orderId()).orElseThrow(() -> new IllegalStateException("Order not found. orderId: " + payload.orderId()));
        order.markStockFailed();
        orderRepository.save(order);

        log.info("Order stock reservation failed. orderId={}, reason={}, correlationId={}, causationId={}",
                payload.orderId(),
                payload.reason(),
                event.getCorrelationId(),
                event.getCausationId()
        );
    }


    @Transactional
    public void markPaymentFailed(MessageEnvelope<PaymentFailedEventPayload> event) {
        PaymentFailedEventPayload payload = event.getPayload();
        Order order = orderRepository.findById(payload.orderId()).orElseThrow(() -> new IllegalStateException("Order not found. orderId: " + payload.orderId()));
        order.markPaymentFailed();
        orderRepository.save(order);
        log.info("Order payment failed. orderId={}, reason={}, correlationId={}, causationId={}",
                payload.orderId(),
                payload.reason(),
                event.getCorrelationId(),
                event.getCausationId()
        );
    }
}
