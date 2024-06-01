package ru.evgeni.microservices.core.discoveryservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.evgeni.microservices.core.discoveryservice.entity.SendEntity;

import java.util.List;

@Repository
public interface OrderCompositeRepository extends CrudRepository<SendEntity, String> {
    @Query(value = "SELECT * FROM send_entity WHERE sendStatus = false FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<SendEntity> findAllEntitiesForSend();
}
