package com.ms.inventory_service.messaging.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.contracts.inventory.ReserveStockCommandPayload;
import com.ms.common.contracts.payment.ProcessPaymentCommandPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.inventory_service.application.InventoryApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReserveStockCommandConsumer {

    private final ObjectMapper objectMapper;
    private final InventoryApplicationService inventoryApplicationService;

    @KafkaListener(
            topics = {Topics.INVENTORY_COMMANDS},
            groupId = "inventory-service-group"
    )
    public void consume(MessageEnvelope<?> message, Acknowledgment acknowledgment){

        if (!MessageTypes.RESERVE_STOCK_COMMAND.equals(message.getMessageType())) {
            log.info("Message ignored. messageType={}", message.getMessageType());
            acknowledgment.acknowledge();
            return;
        }
        ReserveStockCommandPayload payload = objectMapper.convertValue(message.getPayload(), ReserveStockCommandPayload.class);
        MessageEnvelope<ReserveStockCommandPayload> typedMessage = message.withPayload(payload);
        inventoryApplicationService.reserveStock(typedMessage);
        acknowledgment.acknowledge();
    }
}
