package com.ms.inventory_service.domain.model;

import com.ms.inventory_service.domain.enums.StockReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "stock_reservations")
@Getter
@NoArgsConstructor
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private StockReservationStatus status;

    private String failureReason;

    private Instant createdAt;

    public static StockReservation reserved(Long orderId, Long productId, Integer quantity) {
        StockReservation reservation = new StockReservation();
        reservation.orderId = orderId;
        reservation.productId = productId;
        reservation.quantity = quantity;
        reservation.status = StockReservationStatus.RESERVED;
        reservation.createdAt = Instant.now();
        return reservation;
    }

    public static StockReservation failed(Long orderId, Long productId, Integer quantity, String reason) {
        StockReservation reservation = new StockReservation();
        reservation.orderId = orderId;
        reservation.productId = productId;
        reservation.quantity = quantity;
        reservation.status = StockReservationStatus.FAILED;
        reservation.failureReason = reason;
        reservation.createdAt = Instant.now();
        return reservation;
    }
}
