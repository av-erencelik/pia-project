package com.eren.productservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;




@SpringBootApplication
@EnableCaching
public class ProductserviceApplication {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Product Service is running...");
		SpringApplication.run(ProductserviceApplication.class, args);
	}

}
