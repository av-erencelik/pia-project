package com.eren.deliveryservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eren.deliveryservice.model.Driver;
import com.eren.deliveryservice.model.DriverStatus;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(DriverRepository repository) {
        return args -> {
            if(repository.count() == 0) {
                log.info("Preloading " + repository.save(new Driver("Ahmet Yılmaz", DriverStatus.AVAILABLE)));
                log.info("Preloading " + repository.save(new Driver("Elif Demir", DriverStatus.AVAILABLE)));
                log.info("Preloading " + repository.save(new Driver("Mehmet Kaya", DriverStatus.AVAILABLE)));
            }
        };
    }
}
