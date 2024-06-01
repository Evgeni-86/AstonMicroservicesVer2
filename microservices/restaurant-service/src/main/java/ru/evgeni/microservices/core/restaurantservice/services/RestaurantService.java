package ru.evgeni.microservices.core.restaurantservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.evgeni.microservices.core.restaurantservice.entity.RestaurantOrderEntity;
import ru.evgeni.microservices.core.restaurantservice.event.Event;
import ru.evgeni.microservices.core.restaurantservice.model.CustomerOrder;
import ru.evgeni.microservices.core.restaurantservice.repository.RestaurantRepository;

import java.util.Optional;


@Service
public class RestaurantService {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository restaurantRepository;
    private final StreamBridge streamBridge;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, StreamBridge streamBridge) {
        this.restaurantRepository = restaurantRepository;
        this.streamBridge = streamBridge;
    }

    public void save(CustomerOrder customerOrder) {
        String itemInDB = "ITEM";
        if (itemInDB.equals(customerOrder.getItem())) {
            LOG.info("Create restaurant order with code: {}", customerOrder.getCode());
            RestaurantOrderEntity orderEntity = new RestaurantOrderEntity();
            orderEntity.setCode(customerOrder.getCode());
            orderEntity.setStatus("Create");
            restaurantRepository.save(orderEntity);
        } else {
            LOG.debug("Error restaurant order user id {}", customerOrder.getUserId());
            Event<String, CustomerOrder> restaurantReverseEvent = new Event<>(
                    Event.Type.DELETE,
                    customerOrder.getCode(),
                    customerOrder);
            sendMessage("restaurant-result-out-0", restaurantReverseEvent);
        }
    }

    public void reverseOrder(CustomerOrder customerOrder) {
        LOG.debug("Reverse restaurant order code {}", customerOrder.getCode());
        try {
            Optional<RestaurantOrderEntity> order = restaurantRepository.findByCode(customerOrder.getCode());
            order.ifPresent(restaurantRepository::delete);
        } catch (Exception ex) {
            LOG.debug("Error reverse restaurant order code {}", customerOrder.getCode());
        }
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }
}