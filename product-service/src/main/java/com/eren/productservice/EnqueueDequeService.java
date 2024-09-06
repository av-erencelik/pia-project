package com.eren.productservice;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eren.productservice.dto.FoodResponse;

@Service
public class EnqueueDequeService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange exchange;


    public void publishMessage(FoodResponse customerDto){
        rabbitTemplate.convertAndSend(exchange.getName(), "order.created", customerDto);
    }

    public void publishStringMessage(String message){
        String result = (String) rabbitTemplate.convertSendAndReceive(exchange.getName(), "order.deleted", message);
        System.out.println("Result: " + result);
    }

    public void sendDelayedMessage(String message, long delayInMillis) {
        rabbitTemplate.convertAndSend("delayed.exchange", "delayed.delivery", message,
            messagePostProcessor -> {
                messagePostProcessor.getMessageProperties().setDelayLong(delayInMillis);;
                return messagePostProcessor;
            }
        );
    }
}