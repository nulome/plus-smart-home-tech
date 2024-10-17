package ru.test.collect.service.hubs;

import ru.test.collect.mapper.HubEventMapper;
import ru.test.collect.model.HubEvent;
import ru.test.collect.model.HubEventType;
import ru.test.collect.model.scenario.ScenarioRemovedEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(KafkaClient kafkaClient, CollectorTopics topics, HubEventMapper hubEventMapper) {
        super(kafkaClient, topics, hubEventMapper);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent hubEvent = (ScenarioRemovedEvent) event;

        return ScenarioRemovedEventAvro.newBuilder()
                .setName(hubEvent.getName())
                .build();
    }

    @Override
    public void handle(HubEvent event) {
        super.handle(event);
    }
}
