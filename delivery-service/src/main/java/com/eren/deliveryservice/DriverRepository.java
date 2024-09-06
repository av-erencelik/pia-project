package com.eren.deliveryservice;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eren.deliveryservice.model.Driver;

interface DriverRepository  extends JpaRepository<Driver, UUID> {
    
    // List<Driver> findByStatus(DriverStatus status);
}
