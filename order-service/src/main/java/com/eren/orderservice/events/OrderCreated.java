package com.eren.orderservice.events;


import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreated {
    private UUID orderId;
    List<UUID> orderItemIds;
    private double totalPrice;
}
