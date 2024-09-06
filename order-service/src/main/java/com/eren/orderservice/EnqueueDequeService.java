package com.eren.orderservice;

import java.util.List;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eren.orderservice.dto.AvailabilityResponse;
import com.eren.orderservice.events.OrderCancelled;
import com.eren.orderservice.events.OrderCreated;
import com.eren.orderservice.events.OrderRequested;

@Service
public class EnqueueDequeService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;


    public List<AvailabilityResponse> publishOrderRequestedEvent(OrderRequested requestedOrder){
        @SuppressWarnings("unchecked")
        List<AvailabilityResponse> response = (List<AvailabilityResponse>) rabbitTemplate.convertSendAndReceive(exchange.getName(), "order.requested", requestedOrder);
        return response;
    }

    public void publishOrderCreatedEvent(OrderCreated createdOrder){
        rabbitTemplate.convertAndSend(exchange.getName(), "order.created", createdOrder);
    }

    public void publishOrderCancelledEvent(OrderCancelled cancelledOrder){
        rabbitTemplate.convertAndSend(exchange.getName(), "order.cancelled", cancelledOrder);
    }
}