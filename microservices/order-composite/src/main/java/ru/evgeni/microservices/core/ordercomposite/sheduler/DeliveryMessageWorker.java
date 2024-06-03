package ru.evgeni.microservices.core.ordercomposite.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgeni.microservices.core.ordercomposite.dto.RequestDeliveryDto;
import ru.evgeni.microservices.core.ordercomposite.entity.MessageDeliveryEntity;
import ru.evgeni.microservices.core.ordercomposite.event.Event;
import ru.evgeni.microservices.core.ordercomposite.repository.OrderCompositeRepository;

import java.util.List;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Service
public class DeliveryMessageWorker {

    private final OrderCompositeRepository orderCompositeRepository;
    private final StreamBridge streamBridge;

    @Scheduled(fixedDelay = 10_000, initialDelay = 10_000)
    @Transactional
    public void send() {
        List<MessageDeliveryEntity> messageDeliveryEntityList = orderCompositeRepository.findAllEntitiesForSend();
        messageDeliveryEntityList.forEach(message -> {
            Message<Event<String, RequestDeliveryDto>> messageForSent = mapToMessage(message);
            log.debug("Sending a {} message to \"delivery-command-out-0\"", messageForSent.getPayload().getEventType());
            streamBridge.send("delivery-command-out-0", messageForSent);
            message.setSendStatus("true");
        });
        orderCompositeRepository.saveAll(messageDeliveryEntityList);
    }

    @NotNull
    private static Message<Event<String, RequestDeliveryDto>> mapToMessage(MessageDeliveryEntity message) {
        RequestDeliveryDto requestDeliveryDto =
                new RequestDeliveryDto(message.getCode(), message.getAddress());
        Event<String, RequestDeliveryDto> currentMessage = new Event<>(
                Event.Type.CREATE,
                requestDeliveryDto.getCode(),
                requestDeliveryDto);
        return MessageBuilder
                .withPayload(currentMessage)
                .setHeader("partitionKey", currentMessage.getKey())
                .build();
    }
}
