package com.eren.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

import com.eren.orderservice.model.OrderStatus;

@Data
@Builder
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private double totalPrice;
    private Timestamp createdAt;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;
    
}
