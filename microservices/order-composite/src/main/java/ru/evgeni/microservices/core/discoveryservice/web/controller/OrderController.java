package ru.evgeni.microservices.core.discoveryservice.web.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evgeni.microservices.core.discoveryservice.dto.CustomerOrderRequestDto;
import ru.evgeni.microservices.core.discoveryservice.model.CustomerOrder;
import ru.evgeni.microservices.core.discoveryservice.service.OrderCompositeProducerService;
import ru.evgeni.microservices.core.discoveryservice.service.OrderCompositeService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private final OrderCompositeProducerService orderCompositeProducerService;
    private final OrderCompositeService orderCompositeService;

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody CustomerOrderRequestDto customerOrderRequestDto) {
        LOG.info("Create order request");
        //TODO: ИСПОЛЬЗОВАТЬ МАППЕР!!!
        String code = UUID.randomUUID().toString();
        CustomerOrder customerOrder = new CustomerOrder(
                code,
                customerOrderRequestDto.getItem(),
                customerOrderRequestDto.getQuantity(),
                customerOrderRequestDto.getAmount(),
                customerOrderRequestDto.getPaymentMethod(),
                customerOrderRequestDto.getUserId(),
                customerOrderRequestDto.getAddress());

        orderCompositeService.saveOrder(customerOrder);
        orderCompositeProducerService.save(customerOrder);
        return ResponseEntity.ok().body("Заказ принят");
    }
}