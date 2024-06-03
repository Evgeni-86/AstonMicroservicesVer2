package ru.evgeni.microservices.core.restaurantservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.restaurantservice.event.Event;
import ru.evgeni.microservices.core.restaurantservice.model.CustomerOrder;
import ru.evgeni.microservices.core.restaurantservice.services.RestaurantService;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private final RestaurantService restaurantService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleCommandMessageProcessor() {
        return event -> {
            log.info("Process message restaurant created at {}...", event.getEventCreatedAt());
            if (event.getEventType() == Event.Type.CREATE) {
                restaurantService.save(event.getData());
            } else if (event.getEventType() == Event.Type.DELETE) {
                restaurantService.reverseOrder(event.getData());
            }
            log.info("Message processing done!");
        };
    }
}
