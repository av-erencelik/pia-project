package com.eren.deliveryservice.model;

import java.util.UUID;

import org.hibernate.annotations.ColumnTransformer;
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
@Table(name = "driver")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
 
    @Column(name = "name", nullable = false, columnDefinition = "Text")
    @NonNull
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "driver_status")
    @ColumnTransformer(write = "?::driver_status")
    private DriverStatus status;

    public Driver(String name, DriverStatus status) {
        this.name = name;
        this.status = status;
    }

    @Builder
    public static Driver createDriver(String name, DriverStatus status) {
        return new Driver(name, status);
    }
}
