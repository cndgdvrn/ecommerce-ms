package com.ms.order_service.api;


import com.ms.order_service.application.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public String createOrder(){
        Long orderId = orderApplicationService.createOrder();
        return "Order created" + orderId;
    }

}
