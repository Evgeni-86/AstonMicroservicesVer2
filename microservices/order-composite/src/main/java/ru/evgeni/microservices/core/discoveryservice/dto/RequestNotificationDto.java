package ru.evgeni.microservices.core.discoveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RequestNotificationDto {
    private String code;
    private String address;
}
