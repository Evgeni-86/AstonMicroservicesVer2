package ru.evgeni.microservices.core.ordercomposite.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {
    private String code;
    private String item;
    private int quantity;
    private double amount;
    private String paymentMethod;
    private String userId;
    private String address;
}
