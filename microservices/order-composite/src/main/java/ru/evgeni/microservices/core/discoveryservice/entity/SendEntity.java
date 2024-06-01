package ru.evgeni.microservices.core.discoveryservice.entity;

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
@Document(collection = "send_entity")
public class SendEntity {
    @Id
    private String code;
    @Version
    private Integer version;
    private Boolean orderStatus;
    private Boolean paymentStatus;
    private Boolean restaurantStatus;
}
