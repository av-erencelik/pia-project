package com.eren.productservice;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eren.productservice.events.OrderCancelled;
import com.eren.productservice.events.OrderCreated;
import com.eren.productservice.events.OrderRequested;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    
    String exchange = "event-exchange";

    @Bean
    public Queue productQueue() {
        return new Queue("product-service-queue", true);
    }

    @Bean
    public Queue delayedQueue() {
        return new Queue("delayed.queue", true);
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed.exchange", "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingDelayedQueue(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with("delayed.delivery").noargs();
    }

    @Bean
    public Binding productBinding(Queue productQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productQueue).to(exchange).with("order.created");
    }

    @Bean
    public Binding productBinding2(Queue productQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productQueue).to(exchange).with("order.requested");
    }

    @Bean
    public Binding productBinding3(Queue productQueue, TopicExchange exchange) {
        return BindingBuilder.bind(productQueue).to(exchange).with("order.cancelled");
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
            "com.eren.orderservice.events.OrderRequested", OrderRequested.class,
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