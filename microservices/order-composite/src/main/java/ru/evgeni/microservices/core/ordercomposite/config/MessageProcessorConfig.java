package ru.evgeni.microservices.core.ordercomposite.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.ordercomposite.entity.MessageDeliveryEntity;
import ru.evgeni.microservices.core.ordercomposite.event.Event;
import ru.evgeni.microservices.core.ordercomposite.model.CustomerOrder;
import ru.evgeni.microservices.core.ordercomposite.service.OrderCompositeProducerService;
import ru.evgeni.microservices.core.ordercomposite.service.OrderCompositeService;

import java.util.Optional;
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
            switch (event.getEventType()) {
                case CREATE: {
                    Optional<MessageDeliveryEntity> sendEntity = orderCompositeService.getSendEntityByCode(event.getData().getCode());
                    sendEntity.ifPresent(element -> {
                        element.setOrderStatus(true);
                        orderCompositeService.saveEntity(element);
                    });
                    return;
                }
                case DELETE: {
                    orderCompositeProducerService.sendReverseIfOrderNotValid(event);
                    return;
                }
            }
            log.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Event<String, CustomerOrder>> handlePaymentResult() {
        return event -> {
            log.info("Process message handle result payment at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE: {
                    Optional<MessageDeliveryEntity> sendEntity = orderCompositeService.getSendEntityByCode(event.getData().getCode());
                    sendEntity.ifPresent(element -> {
                        element.setPaymentStatus(true);
                        orderCompositeService.saveEntity(element);
                    });
                    return;
                }
                case DELETE: {
                    orderCompositeProducerService.sendReverseIfPaymentNotValid(event);
                    return;
                }
            }
            log.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleRestaurantResult() {
        return event -> {
            log.info("Process message handle result restaurant at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE: {
                    Optional<MessageDeliveryEntity> sendEntity = orderCompositeService.getSendEntityByCode(event.getData().getCode());
                    sendEntity.ifPresent(element -> {
                        element.setRestaurantStatus(true);
                        orderCompositeService.saveEntity(element);
                    });
                    return;
                }
                case DELETE: {
                    orderCompositeProducerService.sendReverseIfRestaurantNotValid(event);
                    return;
                }
            }
            log.info("Message processing done!");
        };
    }
}