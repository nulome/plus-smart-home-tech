package ru;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kafka.KafkaClient;
import ru.kafka.snapshots.SnapshotTopics;
import ru.repository.SensorRepository;
import ru.repository.model.Sensor;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {

    private final Duration consumeAttemptTimeout;
    private final SnapshotTopics topics;
    private final SensorRepository sensorRepository;
    private final Consumer<String, SpecificRecordBase> consumer;

    public SnapshotProcessor(@Value("${app.kafka.consume.attempt.timeout:10000}") Duration consumeAttemptTimeout,
                             SnapshotTopics topics, KafkaClient snapshotKafka, SensorRepository sensorRepository) {
        this.consumeAttemptTimeout = consumeAttemptTimeout;
        this.topics = topics;
        this.sensorRepository = sensorRepository;
        this.consumer = snapshotKafka.getConsumer();
    }

    @Override
    public void run() {

        try {
            consumer.subscribe(List.of(topics.getTopicSnapshots()));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(consumeAttemptTimeout);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    log.info("topic = {}, offset = {}, value = {}", record.topic(), record.offset(), record.value());
                    SensorsSnapshotAvro snapshotAvro = (SensorsSnapshotAvro) record.value();
                    saveSensorDB(snapshotAvro);
                }
            }

        } catch (WakeupException ignored) {
            log.debug("Исключение WakeupException.");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                consumer.poll(Duration.ofMinutes(5));
            } catch (WakeupException e) {
                log.debug("Исключение WakeupException.");
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    private void saveSensorDB(SensorsSnapshotAvro snapshotAvro) {
        if (sensorRepository.existsByIdInAndHubId(snapshotAvro.getSensorsState().keySet(), snapshotAvro.getHubId())) {
            Sensor checkSensor = new Sensor();
            checkSensor.setHubId(snapshotAvro.getHubId());
            sensorRepository.save(checkSensor);
        }
    }
}
