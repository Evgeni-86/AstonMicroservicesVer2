package ru.evgeni.microservices.core.payment.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.evgeni.microservices.core.payment.entity.PaymentEntity;
import ru.evgeni.microservices.core.payment.event.Event;
import ru.evgeni.microservices.core.payment.model.CustomerOrder;
import ru.evgeni.microservices.core.payment.repository.PaymentRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StreamBridge streamBridge;
    private double testBalance = 1000d;

    public void save(CustomerOrder customerOrder) {
        if (testBalance > customerOrder.getAmount()) {
            log.info("Create payment with code: {}", customerOrder.getCode());
            PaymentEntity payment = new PaymentEntity();
            payment.setUserId(customerOrder.getUserId());
            payment.setCode(customerOrder.getCode());
            payment.setSum(customerOrder.getAmount());
            testBalance -= customerOrder.getAmount();
            paymentRepository.save(payment);
        } else {
            log.debug("Error payment user id {}", customerOrder.getUserId());
            Event<String, CustomerOrder> paymentReverseEvent = new Event<>(
                    Event.Type.DELETE,
                    customerOrder.getCode(),
                    customerOrder);
            sendMessage("payment-result-out-0", paymentReverseEvent);
        }
    }

    public void reversePayment(CustomerOrder customerOrder) {
        log.debug("Reverse payment user id {}", customerOrder.getUserId());
        try {
            Optional<PaymentEntity> byCode = paymentRepository.findByCode(customerOrder.getCode());
            byCode.ifPresent(payment -> {
                testBalance += customerOrder.getAmount();
                paymentRepository.deleteByCode(customerOrder.getCode());
            });
        } catch (Exception ex) {
            log.debug("Error reverse payment user id {}", customerOrder.getUserId());
        }
    }

    private void sendMessage(String bindingName, Event event) {
        log.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }
}