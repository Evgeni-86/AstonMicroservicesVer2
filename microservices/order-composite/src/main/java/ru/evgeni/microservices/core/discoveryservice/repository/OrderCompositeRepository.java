package ru.evgeni.microservices.core.discoveryservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.evgeni.microservices.core.discoveryservice.entity.SendEntity;

@Repository
public interface OrderCompositeRepository extends CrudRepository<SendEntity, String> {
}
