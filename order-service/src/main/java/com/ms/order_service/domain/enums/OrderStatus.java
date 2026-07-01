package com.ms.order_service.domain.enums;

public enum OrderStatus {
    PAYMENT_PENDING,
    PAYMENT_COMPLETED,
    PAYMENT_FAILED,
    STOCK_PENDING,
    STOCK_RESERVED,
    STOCK_FAILED,
    CONFIRMED,
    CANCELLED
}
