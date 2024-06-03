package ru.evgeni.microservices.core.ordercomposite.entity;

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
@Table(name = "delivery_messages")
public class MessageDeliveryEntity {
    @Id
    private String code;
    private String address;
    private String orderStatus;
    private String paymentStatus;
    private String restaurantStatus;
    private String sendStatus;
}
