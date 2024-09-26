package collect;

import collect.mapper.HubEventMapper;
import collect.mapper.SensorEventMapper;
import collect.model.HubEvent;
import collect.model.SensorEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;


@Service
@RequiredArgsConstructor
public class ServiceCollectorImpl implements ServiceCollector {

    private final KafkaClient kafkaClient;
    private final HubEventMapper hubEventMapper;
    private final SensorEventMapper sensorEventMapper;


    @Override
    public void sendSensorKafka(SensorEvent event) {
        SensorEventAvro eventAvro = sensorEventMapper.toSensorEventAvro(event);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(CollectorTopics.TOPIC_SENSORS, eventAvro);
        kafkaClient.getProducer().send(record);
    }

    @Override
    public void sendHubKafka(HubEvent event) {
        HubEventAvro eventAvro = hubEventMapper.toHubEventAvro(event);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(CollectorTopics.TOPIC_HUBS, eventAvro);
        kafkaClient.getProducer().send(record);
    }

}
