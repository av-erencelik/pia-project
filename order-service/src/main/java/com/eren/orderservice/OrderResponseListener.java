package com.eren.orderservice;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.eren.orderservice.events.DeliveryCanceled;
import com.eren.orderservice.events.DeliveryCompleted;
import com.eren.orderservice.events.DeliveryStarted;
import com.eren.orderservice.model.OrderStatus;

import lombok.AllArgsConstructor;

@Service
@RabbitListener(queues = "order-service-queue")
@AllArgsConstructor
public class OrderResponseListener {

    private OrderService orderService;


    @RabbitHandler
    public void handleDeliveryStartedMessage(DeliveryStarted deliveryStarted) {
        orderService.updateOrderStatus(deliveryStarted.getOrderId(), OrderStatus.ON_DELIVERY);
    }

    @RabbitHandler
    public void handleDeliveryCompletedMessage(DeliveryCompleted deliveryCompleted) {
        orderService.updateOrderStatus(deliveryCompleted.getOrderId(), OrderStatus.DELIVERED);
    }

    @RabbitHandler
    public void handleDeliveryCanceledMessage(DeliveryCanceled deliveryCanceled) {
        orderService.cancelOrder(deliveryCanceled.getOrderId());
    }
    
}
