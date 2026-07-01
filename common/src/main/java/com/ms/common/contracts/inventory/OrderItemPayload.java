package com.ms.common.contracts.inventory;

public record OrderItemPayload(
        Long productId,
        Integer quantity
) {
}
