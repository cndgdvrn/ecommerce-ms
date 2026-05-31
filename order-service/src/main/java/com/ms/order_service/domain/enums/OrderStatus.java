package com.ms.order_service.domain.enums;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    CONFIRMED,
    CANCELLED
}
