package com.eren.deliveryservice;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eren.deliveryservice.dto.DeliveryResponse;
import com.eren.deliveryservice.dto.DriverResponse;
import com.eren.deliveryservice.events.DeliveryCanceled;
import com.eren.deliveryservice.events.DeliveryCompleted;
import com.eren.deliveryservice.events.DeliveryStarted;
import com.eren.deliveryservice.model.Delivery;
import com.eren.deliveryservice.model.DeliveryStatus;
import com.eren.deliveryservice.model.Driver;
import com.eren.deliveryservice.model.DriverStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final EnqueueDequeService enqueueDequeService;

    public void createDelivery(UUID orderId, UUID driverId) {
        log.info("Creating delivery: {}", orderId);
        Delivery delivery = new Delivery();
        Driver driver = driverRepository.findById(driverId).orElseThrow();
        delivery.setOrderId(orderId);
        delivery.setDriver(driver);
        delivery.setStatus(DeliveryStatus.ON_DELIVERY);
        // one minute expiration date
        delivery.setExpirationDate(new Timestamp(System.currentTimeMillis() + 60000));
        UUID id = deliveryRepository.save(delivery).getId();
        driver.setStatus(DriverStatus.ON_DELIVERY);
        driverRepository.save(driver);

        enqueueDequeService.sendDeliveryStarted(mapDeliveryToDeliveryStarted(delivery));
        enqueueDequeService.sendDeliveryExpiration(id, 60000);
    }

    public List<Driver> getDrivers() {
        log.info("Getting drivers");
        return driverRepository.findAll();
    }

    public UUID createDeliveryWithoutDriver(UUID orderId) {
        log.info("Creating delivery without driver: {}", orderId);
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setStatus(DeliveryStatus.PREPARING);
        return deliveryRepository.save(delivery).getId();
        
    }

    public void attachDriverToDelivery(UUID deliveryId, UUID driverId) {
        log.info("Attaching driver to delivery: {}", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        Driver driver = driverRepository.findById(driverId).orElseThrow();
        delivery.setDriver(driver);
        delivery.setStatus(DeliveryStatus.ON_DELIVERY);
        delivery.setExpirationDate(new Timestamp(System.currentTimeMillis() + 60000));
        deliveryRepository.save(delivery);
        driver.setStatus(DriverStatus.ON_DELIVERY);
        driverRepository.save(driver);

        enqueueDequeService.sendDeliveryStarted(mapDeliveryToDeliveryStarted(delivery));
        enqueueDequeService.sendDeliveryExpiration(deliveryId, 60000);
    }

    public DeliveryStatus getDeliveryStatus(UUID deliveryId) {
        log.info("Getting delivery status: {}", deliveryId);
        return deliveryRepository.findById(deliveryId).orElseThrow().getStatus();
    }

    public boolean checkDriverAvailability(UUID driverId) {
        log.info("Checking driver availability: {}", driverId);
        Optional<Driver> driver = driverRepository.findById(driverId);
        log.info("Driver: {}", driver);
        return driver.isPresent() && driver.get().getStatus() == DriverStatus.AVAILABLE;
    }

    public List<Driver> getAvailableDriver() {
        log.info("Getting available driver");
        List<Driver> drivers = driverRepository.findAll();
        return drivers.stream().filter(driver -> driver.getStatus() == DriverStatus.AVAILABLE).toList();
    }

    public void handleLateDelivery(UUID deliveryId) {
        log.info("Handling late delivery: {}", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        if (delivery.getStatus() == DeliveryStatus.CANCELED) {
           return;
        }
        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            return;
        }
        delivery.setOnTime(false);
        deliveryRepository.save(delivery);
    }

    public void completeDelivery(UUID deliveryId) {
        log.info("Completing delivery: {}", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        if (delivery.getStatus() == DeliveryStatus.CANCELED) {
            throw new IllegalArgumentException("Delivery is already canceled");
        }
        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new IllegalArgumentException("Delivery is already delivered");
        }
        delivery.setStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
        Driver driver = delivery.getDriver();
        driver.setStatus(DriverStatus.AVAILABLE);
        driverRepository.save(driver);
        enqueueDequeService.sendDeliveryCompleted(mapDeliveryToDeliveryCompleted(delivery));
        enqueueDequeService.sendDriverBecomeAvailable(driver.getId());
    }

    public void cancelDelivery(UUID deliveryId) {
        log.info("Cancelling delivery: {}", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        delivery.setStatus(DeliveryStatus.CANCELED);
        deliveryRepository.save(delivery);
        Driver driver = delivery.getDriver();
        driver.setStatus(DriverStatus.AVAILABLE);
        driverRepository.save(driver);
        enqueueDequeService.sendDeliveryCanceled(mapDeliveryToDeliveryCanceled(delivery));
        enqueueDequeService.sendDriverBecomeAvailable(driver.getId());
    }

    public void checkOrderDeliveryCanceled(UUID orderId) {
        log.info("Checking order delivery canceled: {}", orderId);
        List<Delivery> deliveries = deliveryRepository.findByOrderId(orderId);
        Delivery delivery = deliveries.get(0);
        if (delivery != null && delivery.getStatus() != DeliveryStatus.CANCELED) {
            delivery.setStatus(DeliveryStatus.CANCELED);
            deliveryRepository.save(delivery);
            Driver driver = delivery.getDriver();
            driver.setStatus(DriverStatus.AVAILABLE);
            driverRepository.save(driver);
            enqueueDequeService.sendDriverBecomeAvailable(driver.getId());
        }
    }

    public List<DeliveryResponse> getDeliveries(int size, int page) {
        log.info("Getting deliveries");
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);
        return deliveryRepository.findAll(pageable).stream().map(this::MapToDeliveryResponse).toList();
    }

    public DeliveryResponse getDelivery(UUID id) {
        log.info("Getting delivery with id: {}", id);
        Delivery delivery =  deliveryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Delivery not found with id: " + id));
        return MapToDeliveryResponse(delivery);
    }


    private DeliveryCompleted mapDeliveryToDeliveryCompleted(Delivery delivery) {
        return DeliveryCompleted.builder()
                .orderId(delivery.getOrderId())
                .onTime(delivery.getOnTime())
                .build();
    };

    private DeliveryCanceled mapDeliveryToDeliveryCanceled(Delivery delivery) {
        return DeliveryCanceled.builder()
                .orderId(delivery.getOrderId())
                .build();
    };

    private DeliveryStarted mapDeliveryToDeliveryStarted(Delivery delivery) {
        return DeliveryStarted.builder()
                .orderId(delivery.getOrderId())
                .build();
    };

    private DeliveryResponse MapToDeliveryResponse(Delivery delivery) {
        return DeliveryResponse.builder()
                .id(delivery.getId().toString())
                .orderId(delivery.getOrderId().toString())
                .driver(delivery.getDriver() != null ? mapDriverToDriverResponse(delivery.getDriver()) : null)
                .status(delivery.getStatus())
                .expirationDate(delivery.getExpirationDate())
                .onTime(delivery.getOnTime())
                .build();
    }

    private DriverResponse mapDriverToDriverResponse(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId().toString())
                .name(driver.getName())
                .status(driver.getStatus())
                .build();
    }



}
