package com.eren.deliveryservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import com.eren.deliveryservice.model.DriverStatus;

@Data
@Builder
@AllArgsConstructor
public class DriverResponse {
    private String id;
    private String name;
    private DriverStatus status;
    
}
