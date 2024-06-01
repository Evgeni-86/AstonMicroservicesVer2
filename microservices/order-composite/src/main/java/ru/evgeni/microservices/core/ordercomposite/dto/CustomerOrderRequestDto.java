package ru.evgeni.microservices.core.ordercomposite.dto;

import lombok.Data;

@Data
public class CustomerOrderRequestDto {
    private String item;
    private int quantity;
    private double amount;
    private String paymentMethod;
    private String userId;
    private String address;
}
