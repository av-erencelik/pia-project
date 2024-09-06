package com.eren.productservice;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eren.productservice.model.Food;
import java.util.UUID;

interface FoodRepository extends JpaRepository<Food, UUID> {
}