package com.ms.common.contracts.inventory;

public record StockReservationFailedEventPayload(
        Long orderId,
        String reason
) {
}
