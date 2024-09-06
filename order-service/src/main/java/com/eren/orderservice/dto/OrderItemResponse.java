package com.eren.orderservice.dto;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;



@Data
@Builder
@AllArgsConstructor
public class OrderItemResponse {
    private UUID productId;
    private String productName;
    private double price;
    private UUID orderId;
    private Timestamp createdAt;
}
