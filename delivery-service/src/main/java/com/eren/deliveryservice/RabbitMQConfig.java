package com.eren.deliveryservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eren.deliveryservice.events.OrderCancelled;
import com.eren.deliveryservice.events.OrderCreated;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {
    String exchange = "event-exchange";

    @Bean
    public Queue deliveryQueue() {
        return new Queue("delivery-service-queue", true);
    }

    @Bean
    public Queue expirationQueue() {
        return new Queue("delivery-expiration-queue", true);
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed.exchange", "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingDelayedQueue(Queue expirationQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(expirationQueue).to(delayedExchange).with("delivery.expiration").noargs();
    }


    @Bean
    public Binding deliveryBinding(Queue deliveryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deliveryQueue).to(exchange).with("order.created");
    }

    @Bean
    public Binding deliveryBinding2(Queue deliveryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deliveryQueue).to(exchange).with("order.cancelled");
    }

    @Bean
    public Binding binding(Queue deliveryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deliveryQueue).to(exchange).with("driver.available");
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        

        typeMapper.setIdClassMapping(Map.of(
            "com.eren.orderservice.events.OrderCreated", OrderCreated.class,
            "com.eren.orderservice.events.OrderCancelled", OrderCancelled.class
        ));
        
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }


}
