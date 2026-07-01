package com.ms.inventory_service.domain.repository;

import com.ms.inventory_service.domain.model.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockReservationRepository extends JpaRepository<StockReservation,Long> {
}
