package com.ms.common.contracts.payment;

public record PaymentFailedEventPayload(
        Long orderId, Long customerId, String reason
) {
}
