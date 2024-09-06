package com.eren.productservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eren.productservice.dto.AvailabilityRequest;
import com.eren.productservice.dto.AvailabilityResponse;
import com.eren.productservice.dto.FoodRequest;
import com.eren.productservice.dto.FoodRequestParam;
import com.eren.productservice.dto.FoodResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequiredArgsConstructor
public class FoodController {
    

    private final FoodService foodService;
    private final EnqueueDequeService enqueueDequeService;


        @GetMapping("/hello")
        public String getHello() {
            return "Hello!";
        }


        @PostMapping("/")
        @ResponseStatus(HttpStatus.CREATED)
        public String createFood(@Valid @RequestBody FoodRequest foodRequest) {
            return foodService.createFood(foodRequest).toString();
        }

        @GetMapping("/")
        public List<FoodResponse> getFoods(@Valid FoodRequestParam params) {
            return foodService.getFoods(params.getSize(), params.getPage());
        }
        
        @GetMapping("/availability")
        public List<AvailabilityResponse> getMethodName(@Valid AvailabilityRequest availabilityRequest) {
            return foodService.checkAvailability(availabilityRequest.getIds());
        }

        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void updateFood(@Valid @PathVariable UUID id, @Valid @RequestBody FoodRequest foodRequest) {
            foodService.updateFood(id, foodRequest);
        }

        @GetMapping("/{id}")
        public FoodResponse getFood(@Valid @PathVariable UUID id) {
            enqueueDequeService.sendDelayedMessage(getHello(), 30000);
            return foodService.getFood(id);
        }

       
        
        
 }