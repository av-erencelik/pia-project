package com.eren.productservice.dto;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class FoodResponse implements Serializable {
    private String name;
    private double price;
    private int stock;
    private String id;
    private Timestamp createdAt;
}
