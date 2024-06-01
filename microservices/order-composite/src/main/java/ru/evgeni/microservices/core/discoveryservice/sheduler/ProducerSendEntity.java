package ru.evgeni.microservices.core.discoveryservice.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgeni.microservices.core.discoveryservice.dto.RequestNotificationDto;
import ru.evgeni.microservices.core.discoveryservice.entity.SendEntity;
import ru.evgeni.microservices.core.discoveryservice.event.Event;
import ru.evgeni.microservices.core.discoveryservice.repository.OrderCompositeRepository;

import java.util.List;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Service
public class ProducerSendEntity {

    private final OrderCompositeRepository orderCompositeRepository;
    private final StreamBridge streamBridge;

    @Scheduled(fixedDelay = 10_000, initialDelay = 10_000)
    @Transactional
    public void send() {
        List<SendEntity> sendEntityList = orderCompositeRepository.findAllEntitiesForSend();
        sendEntityList.forEach(message -> {
            Message<Event<String, RequestNotificationDto>> messageForSent = mapToMessage(message);
            log.debug("Sending a {} message to \"notification-command-out-0\"", messageForSent.getPayload().getEventType());
            streamBridge.send("notification-command-out-0", messageForSent);
            message.setSendStatus(true);
        });
        orderCompositeRepository.saveAll(sendEntityList);
    }

    @NotNull
    private static Message<Event<String, RequestNotificationDto>> mapToMessage(SendEntity message) {
        RequestNotificationDto requestNotificationDto =
                new RequestNotificationDto(message.getCode(), message.getAddress());
        Event<String, RequestNotificationDto> currentMessage = new Event<>(
                Event.Type.CREATE,
                requestNotificationDto.getCode(),
                requestNotificationDto);
        return MessageBuilder
                .withPayload(currentMessage)
                .setHeader("partitionKey", currentMessage.getKey())
                .build();
    }
}
