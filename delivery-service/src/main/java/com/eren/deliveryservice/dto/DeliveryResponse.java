package com.eren.deliveryservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;

import com.eren.deliveryservice.model.DeliveryStatus;


@Data
@Builder
@AllArgsConstructor
public class DeliveryResponse {

    private String id;
    private String orderId;
    private DriverResponse driver;
    private DeliveryStatus status;
    private Timestamp expirationDate;
    
}
