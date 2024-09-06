package com.eren.orderservice;

import org.springframework.web.bind.annotation.RestController;

import com.eren.orderservice.dto.OrderRequest;
import com.eren.orderservice.dto.OrderRequestParam;
import com.eren.orderservice.dto.OrderResponse;
import com.eren.orderservice.model.Order;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/")
    public String createNewOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/")
    public List<OrderResponse> getOrders(@Valid OrderRequestParam orderRequestParam) {
        return orderService.getOrders(orderRequestParam.getSize(), orderRequestParam.getPage());
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable UUID id) {
        return orderService.getOrder(id);
    }

    @PatchMapping("/{id}/cancel")
    public void cancelOrder(@PathVariable UUID id) {
        orderService.cancelOrder(id);
    }    
    
}
