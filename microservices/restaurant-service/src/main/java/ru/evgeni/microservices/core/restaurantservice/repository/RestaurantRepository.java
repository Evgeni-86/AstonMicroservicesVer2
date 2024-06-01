package ru.evgeni.microservices.core.restaurantservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.evgeni.microservices.core.restaurantservice.entity.RestaurantOrderEntity;

import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<RestaurantOrderEntity, String> {
    Optional<RestaurantOrderEntity> findByCode(String code);
}