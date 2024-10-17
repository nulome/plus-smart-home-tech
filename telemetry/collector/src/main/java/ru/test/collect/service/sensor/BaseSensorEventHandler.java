package ru.test.collect.service.sensor;

import ru.test.collect.model.SensorEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;


@RequiredArgsConstructor
public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    protected final KafkaClient kafkaClient;
    protected final CollectorTopics topics;

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {
        T sensor = mapToAvro(event);
        SensorEventAvro eventAvro = new SensorEventAvro();
        eventAvro.setId(event.getId());
        eventAvro.setHubId(event.getHubId());
        eventAvro.setTimestamp(event.getTimestamp());
        eventAvro.setPayload(sensor);

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topics.topicSensors, eventAvro);
        kafkaClient.getProducer().send(record);
    }
}
