package com.eren.deliveryservice.model;


import java.sql.Timestamp;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
 
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = true)
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "delivery_status")
    @ColumnTransformer(write = "?::delivery_status")
    private DeliveryStatus status;


    @Column(name = "expiration_date", nullable = true, columnDefinition = "TIMESTAMP")
    private Timestamp expirationDate;

    @Column(name = "on_time", nullable = false)
    private Boolean onTime = true;


    public Delivery(UUID orderId, Driver driver, DeliveryStatus status, Timestamp expirationDate, boolean onTime) {
        this.orderId = orderId;
        this.driver = driver;
        this.status = status;
        this.expirationDate = expirationDate;
    }

    @Builder
    public static Delivery createDelivery(UUID orderId, Driver driver, DeliveryStatus status, Timestamp expirationDate, boolean onTime) {
        return new Delivery(orderId, driver, status, expirationDate, onTime);
    }
}
