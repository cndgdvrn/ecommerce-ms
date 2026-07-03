package com.ms.order_service.messaging.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.common.contracts.inventory.StockReservationFailedEventPayload;
import com.ms.common.contracts.inventory.StockReservedEventPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.order_service.application.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final ObjectMapper objectMapper;
    private final OrderApplicationService orderApplicationService;

    @KafkaListener(topics = {Topics.INVENTORY_EVENTS}, groupId = "order-service-group")
    public void consume(MessageEnvelope<?> message, Acknowledgment acknowledgment){
        if(message.getMessageType().equals(MessageTypes.STOCK_RESERVED_EVENT)){
            StockReservedEventPayload payload = objectMapper.convertValue(message.getPayload(), StockReservedEventPayload.class);
            MessageEnvelope<StockReservedEventPayload> typedMessage = message.withPayload(payload);
            orderApplicationService.markStockReserved(typedMessage);
            acknowledgment.acknowledge();
            return;
        }

        if(message.getMessageType().equals(MessageTypes.STOCK_RESERVATION_FAILED_EVENT)){
            StockReservationFailedEventPayload payload = objectMapper.convertValue(message.getPayload(), StockReservationFailedEventPayload.class);
            MessageEnvelope<StockReservationFailedEventPayload> typedMessage = message.withPayload(payload);
            orderApplicationService.markStockFailed(typedMessage);
            acknowledgment.acknowledge();
            return;
        }
        log.info("Message ignored. Message Type: {}", message.getMessageType());
        acknowledgment.acknowledge();
    }

}
