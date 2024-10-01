package collect.service.hubs;

import collect.mapper.HubEventMapper;
import collect.model.HubEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;


@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected final KafkaClient kafkaClient;
    protected final CollectorTopics topics;
    protected final HubEventMapper hubEventMapper;

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        T payload = mapToAvro(event);
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topics.topicHubs, eventAvro);
        kafkaClient.getProducer().send(record);
    }
}
