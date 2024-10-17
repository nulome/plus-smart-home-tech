package ru.test.collect.service.hubs;

import ru.test.collect.mapper.HubEventMapper;
import ru.test.collect.model.HubEvent;
import ru.test.collect.model.HubEventType;
import ru.test.collect.model.scenario.ScenarioAddedEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

import java.util.stream.Collectors;

@Component
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
