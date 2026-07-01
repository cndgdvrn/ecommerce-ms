package com.ms.order_service.api.dto;

import com.ms.order_service.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        Long customerId,
        BigDecimal totalAmount,
        String currency,
        OrderStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}