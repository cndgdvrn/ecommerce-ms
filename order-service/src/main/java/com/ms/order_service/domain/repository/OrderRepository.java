package com.ms.order_service.domain.repository;

import com.ms.order_service.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long > {
}
