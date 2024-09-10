package com.eren.productservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eren.productservice.model.Food;


@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(FoodRepository repository) {
        return args -> {
            if(repository.count() < 4) {
                log.info("Preloading " + repository.save(new Food("Pide", 10, 5)));
                log.info("Preloading " + repository.save(new Food("Lahmacun", 20, 2)));
                log.info("Preloading " + repository.save(new Food("Kebab", 30, 3)));
                log.info("Preloading " + repository.save(new Food("Baklava", 40, 4)));
                log.info("Preloading " + repository.save(new Food("Kunefe", 50, 5)));
            }

        };
    }
}
