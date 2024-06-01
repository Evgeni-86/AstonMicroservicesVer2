package ru.evgeni.microservices.core.orderservice.config;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.orderservice.event.Event;
import ru.evgeni.microservices.core.orderservice.model.CustomerOrder;
import ru.evgeni.microservices.core.orderservice.service.OrderService;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);
    private final OrderService orderService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleCommandMessageProcessor() {
        return event -> {
            LOG.info("Process message command order at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE : {
                    orderService.save(event.getData());
                    return;
                }
                case DELETE : {
                    orderService.remove(event.getData());
                    return;
                }
            }
            LOG.info("Message processing done!");
        };
    }
}

