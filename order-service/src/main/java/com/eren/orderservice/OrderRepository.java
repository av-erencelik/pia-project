package com.eren.orderservice;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eren.orderservice.model.Order;

interface OrderRepository extends JpaRepository<Order, UUID> {
}
