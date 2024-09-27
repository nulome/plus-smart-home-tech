package collect.service.hubs;

import collect.mapper.HubEventMapper;
import collect.model.HubEvent;
import collect.model.HubEventType;
import collect.model.scenario.ScenarioAddedEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import java.util.stream.Collectors;

@Service
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaClient kafkaClient, CollectorTopics topics, HubEventMapper hubEventMapper) {
        super(kafkaClient, topics, hubEventMapper);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent hubEvent = (ScenarioAddedEvent) event;

        return ScenarioAddedEventAvro.newBuilder()
                .setName(hubEvent.getName())
                .setActions(hubEvent.getActions().stream()
                        .map(hubEventMapper::toDeviceActionAvro)
                        .collect(Collectors.toList()))
                .setConditions(hubEvent.getConditions().stream()
                        .map(hubEventMapper::toScenarioConditionAvro)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public void handle(HubEvent event) {
        super.handle(event);
    }
}
