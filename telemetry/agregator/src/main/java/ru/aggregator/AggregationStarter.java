package ru.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.aggregator.kafka.AggregatorTopics;
import ru.aggregator.kafka.KafkaClient;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class AggregationStarter {

    private final Duration consumeAttemptTimeout;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private static final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    private final AggregatorTopics topics;
    private final Consumer<String, SpecificRecordBase> consumer;
    private final Producer<String, SpecificRecordBase> producer;

    public AggregationStarter(@Value("${app.kafka.consume.attempt.timeout:10000}") Duration consumeAttemptTimeout,
                              AggregatorTopics topics, KafkaClient kafkaClient) {
        this.consumeAttemptTimeout = consumeAttemptTimeout;
        this.topics = topics;
        this.consumer = kafkaClient.getConsumer();
        this.producer = kafkaClient.getProducer();
    }


    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        log.trace("start AggregationStarter");
        try {
            consumer.subscribe(List.of(topics.getTopicSensors()));

            while (true) {
                log.info("Check consumer records");
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(consumeAttemptTimeout);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    log.info("topic = {}, offset = {}, value = {}", record.topic(), record.offset(), record.value());
                    SensorEventAvro event = (SensorEventAvro) record.value();
                    Optional<SensorsSnapshotAvro> checkUpdate = updateState(event);

                    if (checkUpdate.isPresent()) {
                        ProducerRecord<String, SpecificRecordBase> producerRecord =
                                new ProducerRecord<>(topics.getTopicSnapshots(), checkUpdate.get());
                        producer.send(producerRecord);

                        snapshots.put(event.getHubId(), checkUpdate.get());
                        log.info("Update snapshots Hub Id: {}", event.getHubId());
                    }

                    currentOffsets.put(
                            new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "")
                    );
                }

                consumer.commitAsync(currentOffsets, null);
            }

        } catch (WakeupException ignored) {
            log.debug("Исключение WakeupException.");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                consumer.commitAsync(currentOffsets, null);
                consumer.poll(Duration.ofMinutes(5));
            } catch (WakeupException e) {
                log.debug("Исключение WakeupException.");
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshotAvro;
        if (snapshots.containsKey(event.getHubId())) {
            snapshotAvro = snapshots.get(event.getHubId());
        } else {
            return Optional.of(createSnapshotAvro(event));
        }

        if (snapshotAvro.getSensorsState().containsKey(event.getId())) {
            SensorStateAvro oldState = snapshotAvro.getSensorsState().get(event.getId());

            if ((event.getTimestamp().equals(oldState.getTimestamp())
                    || event.getTimestamp().isBefore(oldState.getTimestamp()))
                    && event.getPayload().equals(oldState.getData())) {
                return Optional.empty();
            }
        }

        snapshotAvro.getSensorsState().put(event.getId(), createStateAvro(event));
        snapshotAvro.setTimestamp(event.getTimestamp());
        return Optional.of(snapshotAvro);
    }

    private SensorsSnapshotAvro createSnapshotAvro(SensorEventAvro event) {
        HashMap<String, SensorStateAvro> stateAvroHashMap = new HashMap<>();
        stateAvroHashMap.put(event.getId(), createStateAvro(event));

        SensorsSnapshotAvro snapshotAvro = new SensorsSnapshotAvro();
        snapshotAvro.setHubId(event.getHubId());
        snapshotAvro.setTimestamp(Instant.now());
        snapshotAvro.setSensorsState(stateAvroHashMap);
        return snapshotAvro;
    }

    private SensorStateAvro createStateAvro(SensorEventAvro event) {
        SensorStateAvro stateAvro = new SensorStateAvro();
        stateAvro.setTimestamp(event.getTimestamp());
        stateAvro.setData(event.getPayload());
        return stateAvro;
    }
}
