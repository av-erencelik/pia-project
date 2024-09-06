package com.eren.orderservice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eren.orderservice.dto.AvailabilityResponse;
import com.eren.orderservice.dto.OrderItemResponse;
import com.eren.orderservice.dto.OrderRequest;
import com.eren.orderservice.dto.OrderResponse;
import com.eren.orderservice.events.OrderCancelled;
import com.eren.orderservice.events.OrderCreated;
import com.eren.orderservice.events.OrderRequested;
import com.eren.orderservice.model.Order;
import com.eren.orderservice.model.OrderItem;
import com.eren.orderservice.model.OrderStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EnqueueDequeService enqueueDequeService;

    public String createOrder(OrderRequest orderRequest) {
        log.info("Creating order: {}", orderRequest);
        List<AvailabilityResponse> orderedProducts = enqueueDequeService.publishOrderRequestedEvent(mapOrderRequestToEvent(orderRequest));

        if (orderedProducts.isEmpty() || orderedProducts.size() != orderRequest.getProductIds().size()) {
            throw new IllegalArgumentException("No products found with given ids");
        }

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = new ArrayList<>();

        for (AvailabilityResponse availabilityResponse : orderedProducts) {
            if (availabilityResponse.isAvailable() == false) {
                throw new IllegalArgumentException("Product out of stock with id: " + availabilityResponse.getId());
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(availabilityResponse.getId());
            orderItem.setPrice(availabilityResponse.getPrice());
            orderItem.setOrderId(order.getId());
            orderItem.setName(availabilityResponse.getName());
            orderItems.add(orderItem);
        }

        order.setTotalPrice(orderItems.stream().mapToDouble(OrderItem::getPrice).sum());

        orderRepository.save(order).getId();
        orderItemRepository.saveAll(orderItems.stream().map(orderItem -> {
            orderItem.setOrderId(order.getId());
            return orderItem;
        }).toList());

        order.setOrderItems(orderItems);
        enqueueDequeService.publishOrderCreatedEvent(mapOrderToEvent(order));

        return order.getId().toString();

    }

    public void cancelOrder(UUID orderId) {
        log.info("Order cancelled with id: {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalArgumentException("Order already cancelled with id: " + orderId);
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Order already delivered with id: " + orderId);
        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        enqueueDequeService.publishOrderCancelledEvent(mapOrderToEventCancelled(order));
    }

    public Order getOrder(UUID id) {
        log.info("Getting order with id: {}", id);
        Order order =  orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));

        return order;
    }

    public void updateOrderStatus(UUID orderId, OrderStatus orderStatus) {
        log.info("Updating order status with id: {} to {}", orderId, orderStatus);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }

    public List<OrderResponse> getOrders(int size, int page) {
        log.info("Getting all orders");
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository.findAll(pageable).stream().map(this::mapOrderToResponse).toList();
    }

    private OrderRequested mapOrderRequestToEvent(OrderRequest orderRequest) {
        return new OrderRequested(orderRequest.getProductIds());
    }

    private OrderCreated mapOrderToEvent(Order order) {
        return OrderCreated.builder()
                .orderId(order.getId())
                .orderItemIds(order.getOrderItems().stream().map(OrderItem::getProductId).toList())
                .totalPrice(order.getTotalPrice())
                .build();
    }

    private OrderCancelled mapOrderToEventCancelled(Order order) {
        return OrderCancelled.builder()
                .orderId(order.getId())
                .orderItemIds(order.getOrderItems().stream().map(OrderItem::getProductId).toList())
                .totalPrice(order.getTotalPrice())
                .build();
    }

    private OrderResponse mapOrderToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId().toString())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .orderItems(order.getOrderItems().stream().map(orderItem -> OrderItemResponse.builder()
                        .productId(orderItem.getProductId())
                        .productName(orderItem.getName())
                        .price(orderItem.getPrice())
                        .createdAt(orderItem.getCreatedAt())
                        .orderId(orderItem.getOrderId())
                        .build()).toList())
                .build();
    }
    
}
