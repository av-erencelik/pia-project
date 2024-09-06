package com.eren.orderservice.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "total_price", nullable = false, columnDefinition = "Decimal(10,2)")
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "order_status")
    @ColumnTransformer(write = "?::order_status")
    private OrderStatus status;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    public Order(double totalPrice, OrderStatus status) {
        this.totalPrice = totalPrice;
        this.status = status;
    }

    @Builder
    public static Order createOrder(double totalPrice, OrderStatus status) {
        return new Order(totalPrice, status);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }
    
}
