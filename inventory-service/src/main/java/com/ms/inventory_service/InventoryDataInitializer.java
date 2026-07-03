package com.ms.inventory_service;

import com.ms.inventory_service.domain.model.Product;
import com.ms.inventory_service.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class InventoryDataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        Optional<Product> product = productRepository.findById(1L);
        if(product.isEmpty()){
            Product demoProduct = new Product(1L, "Demo Product", 100);
            productRepository.save(demoProduct);
        }
    }

}
