package collect.service.hubs;

import collect.mapper.HubEventMapper;
import collect.model.HubEvent;
import collect.model.HubEventType;
import collect.model.device.DeviceAddedEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;

@Service
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(KafkaClient kafkaClient, CollectorTopics topics, HubEventMapper hubEventMapper) {
        super(kafkaClient, topics, hubEventMapper);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent hubEvent = (DeviceAddedEvent) event;

        return hubEventMapper.toDeviceAddedEventAvro(hubEvent);
    }

    @Override
    public void handle(HubEvent event) {
        super.handle(event);
    }
}
