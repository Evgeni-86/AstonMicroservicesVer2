package ru.evgeni.microservices.core.payment.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.payment.event.Event;
import ru.evgeni.microservices.core.payment.model.CustomerOrder;
import ru.evgeni.microservices.core.payment.services.PaymentService;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private final PaymentService paymentService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleCommandMessageProcessor() {
        return event -> {
            log.info("Process message payment created at {}...", event.getEventCreatedAt());
            switch (event.getEventType()) {
                case CREATE : {
                    paymentService.save(event.getData());
                    return;
                }
                case DELETE : {
                    paymentService.reversePayment(event.getData());
                    return;
                }
            }
            log.info("Message processing done!");
        };
    }
}
