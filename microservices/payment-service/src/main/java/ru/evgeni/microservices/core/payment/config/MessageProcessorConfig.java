package ru.evgeni.microservices.core.payment.config;

import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.evgeni.microservices.core.payment.event.Event;
import ru.evgeni.microservices.core.payment.model.CustomerOrder;
import ru.evgeni.microservices.core.payment.services.PaymentService;

@RequiredArgsConstructor
@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);
    private final PaymentService paymentService;

    @Bean
    public Consumer<Event<String, CustomerOrder>> handleCommandMessageProcessor() {
        return event -> {
            LOG.info("Process message payment created at {}...", event.getEventCreatedAt());
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
            LOG.info("Message processing done!");
        };
    }
}
