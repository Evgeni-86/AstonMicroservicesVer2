package ru.evgeni.microservices.core.discoveryservice.sheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgeni.microservices.core.discoveryservice.dto.RequestNotificationDto;
import ru.evgeni.microservices.core.discoveryservice.entity.SendEntity;
import ru.evgeni.microservices.core.discoveryservice.event.Event;
import ru.evgeni.microservices.core.discoveryservice.repository.OrderCompositeRepository;

import java.util.List;

@EnableScheduling
@RequiredArgsConstructor
@Service
public class ProducerSendEntity {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerSendEntity.class);
    private final OrderCompositeRepository orderCompositeRepository;
    private final StreamBridge streamBridge;

    @Scheduled(fixedDelay = 10_000, initialDelay = 10_000)
    @Transactional
    public void send() {
        List<SendEntity> sendEntityList = orderCompositeRepository.findAllEntitiesForSend();

        sendEntityList.forEach(message -> {
            RequestNotificationDto requestNotificationDto =
                    new RequestNotificationDto(message.getCode(), message.getAddress());

            Event<String, RequestNotificationDto> currentMessage = new Event<>(
                    Event.Type.CREATE,
                    requestNotificationDto.getCode(),
                    requestNotificationDto);

            sendMessage("notification-command-out-0", currentMessage);
            message.setSendStatus(true);
        });

        orderCompositeRepository.saveAll(sendEntityList);
    }

    public void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }
}
