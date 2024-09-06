package com.eren.deliveryservice;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eren.deliveryservice.model.Delivery;

interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    List<Delivery> findByOrderId(UUID orderId);
}

