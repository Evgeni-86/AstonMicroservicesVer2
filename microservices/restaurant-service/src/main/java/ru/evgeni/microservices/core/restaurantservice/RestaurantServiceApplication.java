package ru.evgeni.microservices.core.restaurantservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RestaurantServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
    }

}