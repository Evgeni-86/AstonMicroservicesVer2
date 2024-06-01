package ru.evgeni.microservices.core.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.evgeni.microservices.core.orderservice.entity.OrderEntity;
import ru.evgeni.microservices.core.orderservice.event.Event;
import ru.evgeni.microservices.core.orderservice.model.CustomerOrder;
import ru.evgeni.microservices.core.orderservice.repository.OrderRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    public void save(CustomerOrder customerOrder) {
        LOG.info("Create order with code: {}", customerOrder.getCode());
        try {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setCode(customerOrder.getCode());
            orderEntity.setItem(customerOrder.getItem());
            orderEntity.setQuantity(customerOrder.getQuantity());
            orderEntity.setAmount(customerOrder.getAmount());
            orderEntity.setPaymentMethod(customerOrder.getPaymentMethod());
            orderEntity.setUserId(customerOrder.getUserId());
            orderEntity.setAddress(customerOrder.getAddress());
            orderRepository.save(orderEntity);
        } catch (Exception exception) {
            LOG.debug("Error new order code {}", customerOrder.getCode());
            Event<String, CustomerOrder> orderReverseEvent = new Event<>(
                    Event.Type.DELETE,
                    customerOrder.getCode(),
                    customerOrder);
            sendMessage("order-result-out-0", orderReverseEvent);
        }
    }

    public void remove(CustomerOrder customerOrder) {
        LOG.info("Remove order with code: {}", customerOrder.getCode());
        orderRepository.deleteByCode(customerOrder.getCode());
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }
}
