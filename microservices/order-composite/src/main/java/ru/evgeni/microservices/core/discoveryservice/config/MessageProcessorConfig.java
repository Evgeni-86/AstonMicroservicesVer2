package ru.evgeni.microservices.core.discoveryservice.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.discoveryservice.entity.SendEntity;
import ru.evgeni.microservices.core.discoveryservice.event.Event;
import ru.evgeni.microservices.core.discoveryservice.model.CustomerOrder;
import ru.evgeni.microservices.core.discoveryservice.service.OrderCompositeProducerService;
import ru.evgeni.microservices.core.discoveryservice.service.OrderCompositeSendService;

import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);
    private final OrderCompositeProducerService orderCompositeProducerService;
    private final OrderCompositeSendService orderCompositeSendService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleOrderResult() {
        return event -> {
            LOG.info("Process message handle result order at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE: {
                    Optional<SendEntity> sendEntity = orderCompositeSendService.getSendEntityByCode(event.getData().getCode());
                    sendEntity.ifPresent(element -> {
                        element.setOrderStatus(true);
                        orderCompositeSendService.saveEntity(element);
                    });
                    return;
                }
                case DELETE: {
                    orderCompositeProducerService.sendReverseIfOrderNotValid(event);
                    return;
                }
            }
            LOG.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Event<String, CustomerOrder>> handlePaymentResult() {
        return event -> {
            LOG.info("Process message handle result payment at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE: {
                    Optional<SendEntity> sendEntity = orderCompositeSendService.getSendEntityByCode(event.getData().getCode());
                    sendEntity.ifPresent(element -> {
                        element.setPaymentStatus(true);
                        orderCompositeSendService.saveEntity(element);
                    });
                    return;
                }
                case DELETE: {
                    orderCompositeProducerService.sendReverseIfPaymentNotValid(event);
                    return;
                }
            }
            LOG.info("Message processing done!");
        };
    }

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleRestaurantResult() {
        return event -> {
            LOG.info("Process message handle result restaurant at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE: {
                    Optional<SendEntity> sendEntity = orderCompositeSendService.getSendEntityByCode(event.getData().getCode());
                    sendEntity.ifPresent(element -> {
                        element.setRestaurantStatus(true);
                        orderCompositeSendService.saveEntity(element);
                    });
                    return;
                }
                case DELETE: {
                    orderCompositeProducerService.sendReverseIfRestaurantNotValid(event);
                    return;
                }
            }
            LOG.info("Message processing done!");
        };
    }
}
