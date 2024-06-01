package ru.evgeni.microservices.core.discoveryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.evgeni.microservices.core.discoveryservice.event.Event;
import ru.evgeni.microservices.core.discoveryservice.model.CustomerOrder;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderCompositeProducerService {

    private final StreamBridge streamBridge;

    public void save(CustomerOrder customerOrder) {
        log.info("Create order with code: {}", customerOrder.getCode());

        Event<String, CustomerOrder> orderCreateEvent = new Event<>(
                Event.Type.CREATE,
                customerOrder.getCode(),
                customerOrder);

        sendMessage("order-command-out-0", orderCreateEvent);
        sendMessage("restaurant-command-out-0", orderCreateEvent);
        sendMessage("payment-command-out-0", orderCreateEvent);
    }

    public void sendReverseIfOrderNotValid(Event<String, CustomerOrder> event) {
        Event<String, CustomerOrder> reverseEvent = new Event<>(
                Event.Type.DELETE,
                event.getData().getCode(),
                event.getData());
        sendMessage("restaurant-command-out-0", reverseEvent);
        sendMessage("payment-command-out-0", reverseEvent);
    }

    public void sendReverseIfPaymentNotValid(Event<String, CustomerOrder> event) {
        Event<String, CustomerOrder> reverseEvent = new Event<>(
                Event.Type.DELETE,
                event.getData().getCode(),
                event.getData());
        sendMessage("restaurant-command-out-0", reverseEvent);
        sendMessage("order-command-out-0", reverseEvent);
    }

    public void sendReverseIfRestaurantNotValid(Event<String, CustomerOrder> event) {
        Event<String, CustomerOrder> reverseEvent = new Event<>(
                Event.Type.DELETE,
                event.getData().getCode(),
                event.getData());
        sendMessage("payment-command-out-0", reverseEvent);
        sendMessage("order-command-out-0", reverseEvent);
    }

    public void sendMessage(String bindingName, Event event) {
        log.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }
}
