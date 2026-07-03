package com.ms.order_service.domain.model;

import com.ms.order_service.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private BigDecimal totalAmount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    public Order(Long customerId, BigDecimal totalAmount, String currency) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.status = OrderStatus.PAYMENT_PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void markPaymentCompleted(){
        this.status = OrderStatus.PAYMENT_COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void markPaymentFailed(){
        this.status = OrderStatus.PAYMENT_FAILED;
        this.updatedAt = Instant.now();
    }

    public void markStockPending(){
        this.status = OrderStatus.STOCK_PENDING;
        this.updatedAt = Instant.now();
    }

    public void markStockReserved() {
        this.status = OrderStatus.STOCK_RESERVED;
        this.updatedAt = Instant.now();
    }

    public void markStockFailed() {
        this.status = OrderStatus.STOCK_FAILED;
        this.updatedAt = Instant.now();
    }

    public void markConfirmed() {
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = Instant.now();
    }

    public void markCancelled(){
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }
}
