package com.eren.productservice;

import com.eren.productservice.dto.AvailabilityResponse;
import com.eren.productservice.events.OrderCancelled;
import com.eren.productservice.events.OrderCreated;
import com.eren.productservice.events.OrderRequested;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "product-service-queue")
@AllArgsConstructor
public class FoodResponseListener {

    private FoodService foodService;

    @RabbitHandler
    public void receieveOrderCreatedEvent(OrderCreated message) {
        foodService.decreaseStockByOne(message.getOrderItemIds());
    }

    @RabbitHandler
    public List<AvailabilityResponse> receieveOrderRequestedEvent(OrderRequested message) {
        return foodService.checkAvailability(message.getProductIds());
    }

    @RabbitHandler void receieveOrderCancelledEvent(OrderCancelled message) {
        foodService.increaseStockByOne(message.getOrderItemIds());
    }

    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayedMessage(String message) {
        System.out.println("Received delayed message: " + message);
        // Process the message or notify listeners here
    }

}