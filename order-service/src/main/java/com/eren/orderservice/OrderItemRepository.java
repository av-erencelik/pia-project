package com.eren.orderservice;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eren.orderservice.model.OrderItem;

interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
