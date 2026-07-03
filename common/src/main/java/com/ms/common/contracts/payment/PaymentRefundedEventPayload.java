package com.ms.common.contracts.payment;

import java.math.BigDecimal;

public record PaymentRefundedEventPayload(
        Long orderId,
        Long customerId,
        String refundId,
        BigDecimal refundedAmount,
        String currency,
        String reason
) {
}