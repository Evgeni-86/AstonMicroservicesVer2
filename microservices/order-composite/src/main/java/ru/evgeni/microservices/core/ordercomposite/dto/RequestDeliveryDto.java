package ru.evgeni.microservices.core.ordercomposite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RequestDeliveryDto {
    private String code;
    private String address;
}
