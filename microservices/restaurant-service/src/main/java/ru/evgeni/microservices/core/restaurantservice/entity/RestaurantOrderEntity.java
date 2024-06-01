package ru.evgeni.microservices.core.restaurantservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "restaurantOrder")
public class RestaurantOrderEntity {

    @Id
    private String id;
    @Version
    private Integer version;
    private String code;
    private String status;
}
