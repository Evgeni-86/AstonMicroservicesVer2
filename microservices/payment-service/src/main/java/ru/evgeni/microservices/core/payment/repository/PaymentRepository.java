package ru.evgeni.microservices.core.payment.repository;

import org.springframework.data.repository.CrudRepository;
import ru.evgeni.microservices.core.payment.entity.PaymentEntity;

import java.util.Optional;

public interface PaymentRepository extends CrudRepository<PaymentEntity, String> {
    void deleteByCode(String code);
    Optional<PaymentEntity> findByCode(String code);
}