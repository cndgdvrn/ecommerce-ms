package com.ms.inventory_service.domain.repository;

import com.ms.inventory_service.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
