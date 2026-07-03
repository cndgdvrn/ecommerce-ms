package com.ms.common.contracts.order;

import java.math.BigDecimal;

public record OrderConfirmedEventPayload(
        Long orderId,
        Long customerId,
        BigDecimal totalAmount,
        String currency
) {
}