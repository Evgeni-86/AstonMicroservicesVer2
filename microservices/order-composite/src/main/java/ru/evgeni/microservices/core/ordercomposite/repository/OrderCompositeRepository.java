package ru.evgeni.microservices.core.ordercomposite.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.evgeni.microservices.core.ordercomposite.entity.MessageDeliveryEntity;

import java.util.List;

@Repository
public interface OrderCompositeRepository extends CrudRepository<MessageDeliveryEntity, String> {
    @Query(value =
        """
            SELECT * FROM delivery_messages
            WHERE order_status = true AND payment_status = true AND restaurant_status = true 
            FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    List<MessageDeliveryEntity> findAllEntitiesForSend();
}
