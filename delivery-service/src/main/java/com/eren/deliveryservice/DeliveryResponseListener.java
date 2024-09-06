package com.eren.deliveryservice;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.eren.deliveryservice.events.OrderCancelled;
import com.eren.deliveryservice.events.OrderCreated;
import com.eren.deliveryservice.model.DeliveryStatus;
import com.eren.deliveryservice.model.Driver;

import lombok.AllArgsConstructor;

@Service
@RabbitListener(queues = "delivery-service-queue")
@AllArgsConstructor
public class DeliveryResponseListener {

    private DeliveryService deliveryService;
    private RedisService redisService;
    

    @RabbitListener(queues = "delivery-expiration-queue")
    public void receiveDeliveryExpiration(UUID deliveryId) {
        System.out.println("Delivery expired: " + deliveryId);
        DeliveryStatus status = deliveryService.getDeliveryStatus(deliveryId);
        if (status == DeliveryStatus.ON_DELIVERY) {
            deliveryService.cancelDelivery(deliveryId);
        }
    }

    @RabbitHandler
    public void handleDriverAvailableMessage(UUID driverId) {
        System.out.println("Driver available: " + driverId);
        boolean isDriverAvailable = deliveryService.checkDriverAvailability(driverId);
        if (isDriverAvailable && redisService.getQueueSize().orElse(0L) > 0) {
            UUID deliveryId = redisService.getNextDelivery().map(UUID::fromString).orElseThrow();
            deliveryService.attachDriverToDelivery(deliveryId, driverId);
        }
    }

    @RabbitHandler
    public void handleOrderCreatedMessage(OrderCreated orderCreated) {
        List<Driver> availableDrivers = deliveryService.getAvailableDriver();

        if (availableDrivers.isEmpty()) {
            UUID id = deliveryService.createDeliveryWithoutDriver(orderCreated.getOrderId());
            redisService.addToQueue(id.toString());
        } else {
            Driver driver = availableDrivers.get(0);
            deliveryService.createDelivery(orderCreated.getOrderId(), driver.getId());
        }
    }

    @RabbitHandler
    public void handleOrderCancelledMessage(OrderCancelled orderCancelled) {
        deliveryService.checkOrderDeliveryCanceled(orderCancelled.getOrderId());
    }
}
