package ru.evgeni.microservices.core.orderservice.repository;

import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.repository.CrudRepository;
import ru.evgeni.microservices.core.orderservice.entity.OrderEntity;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, String> {
    public Optional<OrderEntity> findByCode(String code);
    void deleteByCode(String code);
}
