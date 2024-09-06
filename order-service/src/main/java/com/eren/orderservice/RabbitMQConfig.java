package com.eren.orderservice;

import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eren.orderservice.dto.AvailabilityResponse;
import com.eren.orderservice.events.DeliveryCanceled;
import com.eren.orderservice.events.DeliveryCompleted;
import com.eren.orderservice.events.DeliveryStarted;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    String exchange = "event-exchange";

    @Bean
    public Queue orderQueue() {
        return new Queue("order-service-queue", true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    Binding binding(Queue orderQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderQueue).to(exchange).with("delivery.started");
    }

    @Bean
    Binding binding2(Queue orderQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderQueue).to(exchange).with("delivery.completed");
    }

    @Bean
    Binding binding3(Queue orderQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderQueue).to(exchange).with("delivery.canceled");
    }
    

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        

        typeMapper.setIdClassMapping(Map.of(
            "com.eren.productservice.dto.AvailabilityResponse", AvailabilityResponse.class,
            "com.eren.deliveryservice.events.DeliveryStarted", DeliveryStarted.class,
            "com.eren.deliveryservice.events.DeliveryCompleted", DeliveryCompleted.class,
            "com.eren.deliveryservice.events.DeliveryCanceled", DeliveryCanceled.class
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
