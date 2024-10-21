package ru.test.collect.service.hubs;

import ru.test.collect.mapper.HubEventMapper;
import ru.test.collect.model.HubEvent;
import ru.test.collect.model.HubEventType;
import ru.test.collect.model.device.DeviceRemovedEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedEventHandler(KafkaClient kafkaClient, CollectorTopics topics, HubEventMapper hubEventMapper) {
        super(kafkaClient, topics, hubEventMapper);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent hubEvent = (DeviceRemovedEvent) event;

        return DeviceRemovedEventAvro.newBuilder()
                .setId(hubEvent.getId())
                .build();
    }

    @Override
    public void handle(HubEvent event) {
        super.handle(event);
    }
}
