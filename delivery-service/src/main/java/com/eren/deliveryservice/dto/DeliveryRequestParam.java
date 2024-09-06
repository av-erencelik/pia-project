package com.eren.deliveryservice.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequestParam {
    private String query;
    @Min(0)
    private int page = 0;
    @Min(1)
    private int size = 10;

}