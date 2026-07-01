package com.ms.inventory_service.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Table
@Entity(name = "products")
@NoArgsConstructor
@Getter
public class Product {

    @Id
    private Long id;
    private String name;
    private Integer availableQuantity;

    private Instant createdAt;
    private Instant updatedAt;

    public Product(Long id, String name, Integer availableQuantity) {
        this.id = id;
        this.name = name;
        this.availableQuantity = availableQuantity;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void reserve(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }

        if (availableQuantity < quantity) {
            throw new IllegalStateException("Insufficient stock. productId=" + id);
        }

        this.availableQuantity -= quantity;
        this.updatedAt = Instant.now();
    }

}
