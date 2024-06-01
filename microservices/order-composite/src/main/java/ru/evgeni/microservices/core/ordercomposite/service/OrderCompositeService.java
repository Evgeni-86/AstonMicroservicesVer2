package ru.evgeni.microservices.core.ordercomposite.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evgeni.microservices.core.ordercomposite.entity.MessageDeliveryEntity;
import ru.evgeni.microservices.core.ordercomposite.model.CustomerOrder;
import ru.evgeni.microservices.core.ordercomposite.repository.OrderCompositeRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderCompositeService {

    private final OrderCompositeRepository orderCompositeRepository;

    public void saveOrder(CustomerOrder customerOrder) {
        MessageDeliveryEntity messageDeliveryEntity = new MessageDeliveryEntity();
        messageDeliveryEntity.setCode(customerOrder.getCode());
        messageDeliveryEntity.setAddress(customerOrder.getAddress());
        messageDeliveryEntity.setOrderStatus(false);
        messageDeliveryEntity.setPaymentStatus(false);
        messageDeliveryEntity.setRestaurantStatus(false);
        messageDeliveryEntity.setSendStatus(false);
        orderCompositeRepository.save(messageDeliveryEntity);
    }

    public void saveEntity(MessageDeliveryEntity messageDeliveryEntity) {
        orderCompositeRepository.save(messageDeliveryEntity);
    }

    public Optional<MessageDeliveryEntity> getSendEntityByCode(String code) {
        return orderCompositeRepository.findById(code);
    }
}
