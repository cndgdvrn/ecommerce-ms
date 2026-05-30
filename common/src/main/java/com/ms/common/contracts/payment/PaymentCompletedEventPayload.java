package com.ms.common.contracts.payment;

import java.math.BigDecimal;

public record PaymentCompletedEventPayload(
        Long orderId, Long customerId, String paymentId, BigDecimal paidAmount, String currency
){
}
