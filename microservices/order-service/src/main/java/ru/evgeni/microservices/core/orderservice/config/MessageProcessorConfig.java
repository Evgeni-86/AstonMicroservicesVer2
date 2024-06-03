package ru.evgeni.microservices.core.orderservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.orderservice.event.Event;
import ru.evgeni.microservices.core.orderservice.model.CustomerOrder;
import ru.evgeni.microservices.core.orderservice.service.OrderService;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private final OrderService orderService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleCommandMessageProcessor() {
        return event -> {
            log.info("Process message command order at {}...", event.getEventCreatedAt());
            if (event.getEventType() == Event.Type.CREATE) {
                orderService.save(event.getData());
            } else if (event.getEventType() == Event.Type.DELETE) {
                orderService.remove(event.getData());
            }
            log.info("Message processing done!");
        };
    }
}

