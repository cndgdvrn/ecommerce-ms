package com.ms.inventory_service.application;


import com.ms.common.contracts.inventory.OrderItemPayload;
import com.ms.common.contracts.inventory.ReserveStockCommandPayload;
import com.ms.common.contracts.inventory.StockReservationFailedEventPayload;
import com.ms.common.contracts.inventory.StockReservedEventPayload;
import com.ms.common.messaging.MessageEnvelope;
import com.ms.common.messaging.MessageTypes;
import com.ms.common.messaging.Topics;
import com.ms.inventory_service.domain.model.Product;
import com.ms.inventory_service.domain.model.StockReservation;
import com.ms.inventory_service.domain.repository.ProductRepository;
import com.ms.inventory_service.domain.repository.StockReservationRepository;
import com.ms.inventory_service.messaging.publisher.KafkaMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryApplicationService {

    private final ProductRepository productRepository;
    private final StockReservationRepository stockReservationRepository;
    private final KafkaMessagePublisher kafkaMessagePublisher;

    public void reserveStock(MessageEnvelope<ReserveStockCommandPayload> command) {

        try {
            ReserveStockCommandPayload payload = command.getPayload();
            for (OrderItemPayload item : payload.items()) {
                Product product = productRepository.findById(item.productId())
                        .orElseThrow(() -> new IllegalStateException("Product not found. productId=" + item.productId()));

                product.reserve(item.quantity());
                productRepository.save(product);
                StockReservation reserved = StockReservation.reserved(payload.orderId(), item.productId(), item.quantity());
                stockReservationRepository.save(reserved);
            }
            StockReservedEventPayload eventPayload = new StockReservedEventPayload(payload.orderId());
            MessageEnvelope<StockReservedEventPayload> event = MessageEnvelope.from(command, MessageTypes.STOCK_RESERVED_EVENT, eventPayload);
            kafkaMessagePublisher.publish(Topics.INVENTORY_EVENTS, payload.orderId().toString(), event);
        }catch (Exception e){
            handleReservationFailure(command, e.getMessage());
        }
    }

    private void handleReservationFailure(MessageEnvelope<ReserveStockCommandPayload> command, String message) {
        ReserveStockCommandPayload payload = command.getPayload();
        for (OrderItemPayload item : payload.items()) {
            StockReservation failed = StockReservation.failed(payload.orderId(), item.productId(), item.quantity(), message);
            stockReservationRepository.save(failed);
        }
        StockReservationFailedEventPayload eventPayload = new StockReservationFailedEventPayload(payload.orderId(), message);
        MessageEnvelope<StockReservationFailedEventPayload> envelope = MessageEnvelope.from(command, MessageTypes.STOCK_RESERVATION_FAILED_EVENT, eventPayload);
        kafkaMessagePublisher.publish(Topics.INVENTORY_EVENTS, payload.orderId().toString(), envelope);


    }
}
