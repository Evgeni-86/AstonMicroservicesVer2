package ru.evgeni.microservices.core.ordercomposite.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgeni.microservices.core.ordercomposite.entity.MessageDeliveryEntity;
import ru.evgeni.microservices.core.ordercomposite.model.CustomerOrder;
import ru.evgeni.microservices.core.ordercomposite.repository.OrderCompositeRepository;

//@RequiredArgsConstructor
@Service
public class OrderCompositeService {

    @Autowired
    private OrderCompositeRepository orderCompositeRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final String changeOrderStatus = "UPDATE MessageDeliveryEntity c SET c.orderStatus = :status WHERE c.code = :code";
    private final String changePaymentStatus = "UPDATE MessageDeliveryEntity c SET c.paymentStatus = :status WHERE c.code = :code";
    private final String changeRestaurantStatus = "UPDATE MessageDeliveryEntity c SET c.restaurantStatus = :status WHERE c.code = :code";

    public void saveOrder(CustomerOrder customerOrder) {
        MessageDeliveryEntity messageDeliveryEntity = new MessageDeliveryEntity();
        messageDeliveryEntity.setCode(customerOrder.getCode());
        messageDeliveryEntity.setAddress(customerOrder.getAddress());
        messageDeliveryEntity.setOrderStatus("false");
        messageDeliveryEntity.setPaymentStatus("false");
        messageDeliveryEntity.setRestaurantStatus("false");
        messageDeliveryEntity.setSendStatus("false");
        orderCompositeRepository.save(messageDeliveryEntity);
    }

    @Transactional
    public void changeStatus(CustomerOrder customerOrder, String resulType) {
        System.out.println("entityManager " + entityManager);
        MessageDeliveryEntity messageDeliveryEntity = entityManager.find(MessageDeliveryEntity.class, customerOrder.getCode());
        System.out.println("MessageDeliveryEntity " + messageDeliveryEntity);

        if (messageDeliveryEntity != null) {
            String currentQuery = null;
            switch (resulType) {
                case "orderResult":
                    currentQuery = changeOrderStatus;
                    break;
                case "paymentResult":
                    currentQuery = changePaymentStatus;
                    break;
                case "restaurantResult":
                    currentQuery = changeRestaurantStatus;
                    break;
            }

            if (currentQuery != null) {
                entityManager.createQuery(currentQuery)
                        .setParameter("status", "true")
                        .setParameter("code", customerOrder.getCode())
                        .executeUpdate();
            }
        }
    }
}
