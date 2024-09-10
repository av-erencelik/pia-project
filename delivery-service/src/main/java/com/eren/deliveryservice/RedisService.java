package com.eren.deliveryservice;

import java.util.Optional;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final ListOperations<String, String> listOps;
    private static final String DELIVERY_QUEUE = "deliveryQueue";

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.listOps = redisTemplate.opsForList();
    }

    public void addToQueue(String deliveryId) {
        listOps.rightPush(DELIVERY_QUEUE, deliveryId);
    }

    public Optional<String> getNextDelivery() {
        String deliveryId = listOps.leftPop(DELIVERY_QUEUE);
        return Optional.ofNullable(deliveryId);
    }

    public void removeFromQueue(String deliveryId) {
        listOps.remove(DELIVERY_QUEUE, 1, deliveryId); // Removes the first occurrence of the specified element
        
    }

    public Optional<Long> getQueueSize() {
        Long size = listOps.size(DELIVERY_QUEUE);
        return Optional.ofNullable(size);
    }
    
}
