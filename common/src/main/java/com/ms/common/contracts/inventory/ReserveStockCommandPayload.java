package com.ms.common.contracts.inventory;

import java.util.List;

public record ReserveStockCommandPayload(
        Long orderId,
        List<OrderItemPayload> items
) {
}
