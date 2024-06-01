package ru.evgeni.microservices.core.restaurantservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.evgeni.microservices.core.restaurantservice.entity.RestaurantOrderEntity;
import ru.evgeni.microservices.core.restaurantservice.event.Event;
import ru.evgeni.microservices.core.restaurantservice.model.CustomerOrder;
import ru.evgeni.microservices.core.restaurantservice.repository.RestaurantRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final StreamBridge streamBridge;

    public void save(CustomerOrder customerOrder) {
        String itemInDB = "ITEM";
        if (itemInDB.equals(customerOrder.getItem())) {
            log.info("Create restaurant order with code: {}", customerOrder.getCode());
            RestaurantOrderEntity orderEntity = new RestaurantOrderEntity();
            orderEntity.setCode(customerOrder.getCode());
            orderEntity.setStatus("Create");
            restaurantRepository.save(orderEntity);
        } else {
            log.debug("Error restaurant order user id {}", customerOrder.getUserId());
            Event<String, CustomerOrder> restaurantReverseEvent = new Event<>(
                    Event.Type.DELETE,
                    customerOrder.getCode(),
                    customerOrder);
            sendMessage("restaurant-result-out-0", restaurantReverseEvent);
        }
    }

    public void reverseOrder(CustomerOrder customerOrder) {
        log.debug("Reverse restaurant order code {}", customerOrder.getCode());
        try {
            Optional<RestaurantOrderEntity> order = restaurantRepository.findByCode(customerOrder.getCode());
            order.ifPresent(restaurantRepository::delete);
        } catch (Exception ex) {
            log.debug("Error reverse restaurant order code {}", customerOrder.getCode());
        }
    }

    private void sendMessage(String bindingName, Event event) {
        log.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }
}