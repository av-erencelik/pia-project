package com.eren.productservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import java.util.UUID;

import org.springframework.lang.NonNull;

import java.sql.Timestamp;
import java.util.Objects;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "food")
public class Food {
 
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "Text")
    @NonNull
    private String name;

    @Column(name = "price", nullable = false, columnDefinition = "Decimal(10,2)")
    private double price;

    @Column(name = "stock", nullable = false, columnDefinition = "Integer")
    private int stock;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;
    
    public Food(@NonNull String name, double price, int stock) {
        this.name =  Objects.requireNonNull(name, "Name cannot be null");;
        this.price = price;
        this.stock = stock;
    }

    @Builder
    public static Food createFood(@NonNull String name, double price, int stock) {
        return new Food(name, price, stock);
    }
}
