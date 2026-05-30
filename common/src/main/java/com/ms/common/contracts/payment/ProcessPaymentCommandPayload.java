package com.ms.common.contracts.payment;

import java.math.BigDecimal;

public record ProcessPaymentCommandPayload(
        Long orderId, Long customerId, BigDecimal amount, String currency) {
}
