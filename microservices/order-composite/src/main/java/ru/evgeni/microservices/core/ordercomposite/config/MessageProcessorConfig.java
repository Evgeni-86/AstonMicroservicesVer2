package ru.evgeni.microservices.core.ordercomposite.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.ordercomposite.event.Event;
import ru.evgeni.microservices.core.ordercomposite.model.CustomerOrder;
import ru.evgeni.microservices.core.ordercomposite.service.OrderCompositeProducerService;
import ru.evgeni.microservices.core.ordercomposite.service.OrderCompositeService;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private final OrderCompositeProducerService orderCompositeProducerService;
    private final OrderCompositeService orderCompositeService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleOrderResult() {
        return event -> {
            log.info("Process message handle result order at {}...", event.getEventCreatedAt());
            if (event.getEventType() == Event.Type.CREATE) {
                orderCompositeService.changeStatus(event.getData(), "orderResult");
            } else if (event.getEventType() == Event.Type.DELETE) {
                orderCompositeProducerService.sendReverseIfOrderNotValid(event);
            }
            log.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Event<String, CustomerOrder>> handlePaymentResult() {
        return event -> {
            log.info("Process message handle result payment at {}...", event.getEventCreatedAt());
            System.out.println("EVEN " + event.getEventType());
            if (event.getEventType() == Event.Type.CREATE) {
                orderCompositeService.changeStatus(event.getData(), "paymentResult");
            } else if (event.getEventType() == Event.Type.DELETE) {
                orderCompositeProducerService.sendReverseIfPaymentNotValid(event);
            }
            log.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleRestaurantResult() {
        return event -> {
            log.info("Process message handle result restaurant at {}...", event.getEventCreatedAt());
            if (event.getEventType() == Event.Type.CREATE) {
                orderCompositeService.changeStatus(event.getData(), "restaurantResult");
            } else if (event.getEventType() == Event.Type.DELETE) {
                orderCompositeProducerService.sendReverseIfRestaurantNotValid(event);
            }
            log.info("Message processing done!");
        };
    }
}
