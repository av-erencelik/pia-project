package com.eren.orderservice.model;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.lang.NonNull;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, columnDefinition = "Text")
    @NonNull
    private String name;

    @Column(name = "price", nullable = false, columnDefinition = "Decimal(10,2)")
    private double price;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "product_id" , nullable = false)
    private UUID productId;

    public OrderItem(@NonNull String name, double price, UUID orderId) {
        this.name = name;
        this.price = price;
        this.orderId = orderId;
    }

    @Builder
    public static OrderItem createOrderItem(@NonNull String name, double price, UUID orderId) {
        return new OrderItem(name, price, orderId);
    }
    
}
