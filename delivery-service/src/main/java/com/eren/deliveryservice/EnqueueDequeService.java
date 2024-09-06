package com.eren.deliveryservice;

import java.util.UUID;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eren.deliveryservice.events.DeliveryCanceled;
import com.eren.deliveryservice.events.DeliveryCompleted;
import com.eren.deliveryservice.events.DeliveryStarted;

@Service
public class EnqueueDequeService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;
   
    
    public void sendDeliveryExpiration(UUID deliveryId, long delayInMillis) {
        rabbitTemplate.convertAndSend("delayed.exchange", "delivery.expiration", deliveryId,
            messagePostProcessor -> {
                messagePostProcessor.getMessageProperties().setDelayLong(delayInMillis);;
                return messagePostProcessor;
            }
        );
    }

    public void sendDeliveryCompleted(DeliveryCompleted delivery) {
        rabbitTemplate.convertAndSend(exchange.getName(), "delivery.completed", delivery);
    }

    public void sendDeliveryCanceled(DeliveryCanceled delivery) {
        rabbitTemplate.convertAndSend(exchange.getName(), "delivery.canceled", delivery);
    }

    public void sendDeliveryStarted(DeliveryStarted delivery) {
        rabbitTemplate.convertAndSend(exchange.getName(), "delivery.started", delivery);
    }

    public void sendDriverBecomeAvailable(UUID driverId) {
        rabbitTemplate.convertAndSend(exchange.getName(), "driver.available", driverId);
    }
}
