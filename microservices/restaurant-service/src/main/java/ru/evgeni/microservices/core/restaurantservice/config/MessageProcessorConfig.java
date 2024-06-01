package ru.evgeni.microservices.core.restaurantservice.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.restaurantservice.event.Event;
import ru.evgeni.microservices.core.restaurantservice.model.CustomerOrder;
import ru.evgeni.microservices.core.restaurantservice.services.RestaurantService;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);
    private final RestaurantService restaurantService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleCommandMessageProcessor() {
        return event -> {
            LOG.info("Process message restaurant created at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE : {
                    restaurantService.save(event.getData());
                    return;
                }
                case DELETE : {
                    restaurantService.reverseOrder(event.getData());
                    return;
                }
            }
            LOG.info("Message processing done!");
        };
    }
}
