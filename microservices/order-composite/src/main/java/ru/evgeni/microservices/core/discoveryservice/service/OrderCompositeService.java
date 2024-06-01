package ru.evgeni.microservices.core.discoveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.evgeni.microservices.core.discoveryservice.entity.SendEntity;
import ru.evgeni.microservices.core.discoveryservice.model.CustomerOrder;
import ru.evgeni.microservices.core.discoveryservice.repository.OrderCompositeRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderCompositeService {

    private final OrderCompositeRepository orderCompositeRepository;

    public void saveOrder(CustomerOrder customerOrder) {
        SendEntity sendEntity = new SendEntity();
        sendEntity.setCode(customerOrder.getCode());
        sendEntity.setAddress(customerOrder.getAddress());
        sendEntity.setOrderStatus(false);
        sendEntity.setPaymentStatus(false);
        sendEntity.setRestaurantStatus(false);
        sendEntity.setSendStatus(false);
        orderCompositeRepository.save(sendEntity);
    }

    public void saveEntity(SendEntity sendEntity) {
        orderCompositeRepository.save(sendEntity);
    }

    public Optional<SendEntity> getSendEntityByCode(String code) {
        return orderCompositeRepository.findById(code);
    }
}
