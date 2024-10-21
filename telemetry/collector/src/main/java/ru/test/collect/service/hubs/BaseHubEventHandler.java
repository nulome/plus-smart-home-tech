package ru.test.collect.service.hubs;

import ru.test.collect.mapper.HubEventMapper;
import ru.test.collect.model.HubEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
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
        HubEventAvro eventAvro = new HubEventAvro();
        eventAvro.setHubId(event.getHubId());
        eventAvro.setTimestamp(event.getTimestamp());
        eventAvro.setPayload(payload);

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topics.getTopicHubs(), eventAvro);
        kafkaClient.getProducer().send(record);
    }
}
