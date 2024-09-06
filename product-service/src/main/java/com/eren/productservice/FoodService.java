package com.eren.productservice;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eren.productservice.dto.AvailabilityResponse;
import com.eren.productservice.dto.FoodRequest;
import com.eren.productservice.dto.FoodResponse;
import com.eren.productservice.model.Food;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FoodService {

    private final FoodRepository foodRepository;


    @CacheEvict(value = "foods", allEntries = true)
    public UUID createFood(FoodRequest foodRequest) {
        log.info("Creating food: {}", foodRequest);
        Food food = new Food(foodRequest.getName(), foodRequest.getPrice(), foodRequest.getStock());
        return foodRepository.save(food).getId();
    }
    
    @Cacheable(value = "foods")
    public List<FoodResponse> getFoods(int size, int page) {
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);
        List<Food> foods = foodRepository.findAll(pageable).toList();
        
        return foods.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @CacheEvict(value = "foods", allEntries = true)
    public void updateFood(UUID id, FoodRequest foodRequest) {
        log.info("Updating food with id: {}", id);
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + id));
        food.setName(foodRequest.getName());
        food.setPrice(foodRequest.getPrice());
        food.setStock(foodRequest.getStock());
        foodRepository.save(food);
    }

    public FoodResponse getFood(UUID id) {
        log.info("Getting food with id: {}", id);
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food not found with id: " + id));
        return mapToResponse(food);
    }

    public List<AvailabilityResponse> checkAvailability(List<UUID> ids) {
        log.info("Checking availability for ids: {}", ids);
        return foodRepository.findAllById(ids).stream().map(this::mapToAvailabilityResponse).toList();
    }

    @CacheEvict(value = "foods", allEntries = true)
    public void decreaseStockByOne(List<UUID> ids) {
        log.info("Decreasing stock by one for ids: {}", ids);
        List<Food> foods = foodRepository.findAllById(ids);
        foods.forEach(food -> {
            if (food.getStock() == 0) {
                throw new IllegalArgumentException("Stock is not enough for food with id: " + food.getId());
            }
            food.setStock(food.getStock() - 1);
        });
        log.info(null, foods);
        foodRepository.saveAll(foods);
    }

    @CacheEvict(value = "foods", allEntries = true)
    public void increaseStockByOne(List<UUID> ids) {
        log.info("Increasing stock by one for id: {}", ids);

        List<Food> foods = foodRepository.findAllById(ids);
        foods.forEach(food -> {
            food.setStock(food.getStock() + 1);
        });
        foodRepository.saveAll(foods);
        
    }
    
    

    private FoodResponse mapToResponse(Food food) {
        return FoodResponse.builder()
                .name(food.getName())
                .price(food.getPrice())
                .stock(food.getStock())
                .id(food.getId().toString())
                .createdAt(food.getCreatedAt())
                .build();
    }

    private AvailabilityResponse mapToAvailabilityResponse(Food food) {
        return AvailabilityResponse.builder()
                .id(food.getId())
                .isAvailable(food.getStock() > 0)
                .name(food.getName())
                .price(food.getPrice())
                .build();
    }
    
}
