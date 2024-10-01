package collect.service.hubs;

import collect.mapper.HubEventMapper;
import collect.model.HubEvent;
import collect.model.HubEventType;
import collect.model.scenario.ScenarioRemovedEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Service
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
