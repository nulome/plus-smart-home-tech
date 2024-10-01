package collect.service.sensor;

import collect.model.SensorEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
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
        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setPayload(sensor)
                .build();
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topics.topicSensors, eventAvro);
        kafkaClient.getProducer().send(record);
    }
}
