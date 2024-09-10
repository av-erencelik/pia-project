package com.eren.deliveryservice;

import org.springframework.web.bind.annotation.RestController;

import com.eren.deliveryservice.dto.DeliveryRequestParam;
import com.eren.deliveryservice.dto.DeliveryResponse;
import com.eren.deliveryservice.model.Driver;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;






@RestController
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping("/")
    public List<DeliveryResponse> getDeliveries(@Valid DeliveryRequestParam deliveryRequestParam) {
        return deliveryService.getDeliveries(deliveryRequestParam.getSize(), deliveryRequestParam.getPage());
    }

    @GetMapping("/drivers/all")
    public List<Driver> getMethodName() {
        return deliveryService.getDrivers();
    }
    

    @GetMapping("/{id}")
    public DeliveryResponse getMethodName(@PathVariable UUID id) {
        return deliveryService.getDelivery(id);
    }

    @PatchMapping("/{id}/deliver")
    public void deliverOrder(@PathVariable UUID id) {
        deliveryService.completeDelivery(id);;
    }

    
    
    
}
