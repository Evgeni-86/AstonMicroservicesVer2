package ru.evgeni.microservices.core.discoveryservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "send_entity")
public class SendEntity {
    @Id
    private String code;
    private String address;
    private Boolean orderStatus;
    private Boolean paymentStatus;
    private Boolean restaurantStatus;
    private boolean sendStatus;
}
