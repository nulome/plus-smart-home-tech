package ru.aggregator;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AggregationStarter {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(100000);

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private static final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    private final AggregatorTopics topics;

    private final KafkaClient kafkaClient;


    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer();
        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer();

        try {
            consumer.subscribe(List.of(topics.topicSensors));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    System.out.printf("topic = %s, offset = %d, value = %s%n", record.topic(), record.offset(), record.value());
                    SensorEventAvro event = (SensorEventAvro) record.value();
                    Optional<SensorsSnapshotAvro> checkUpdate = updateState(event);

                    if (checkUpdate.isPresent()) {
                        ProducerRecord<String, SpecificRecordBase> producerRecord =
                                new ProducerRecord<>(topics.topicSnapshots, checkUpdate.get());
                        producer.send(producerRecord);

                        snapshots.put(event.getHubId(), checkUpdate.get());
                        System.out.println("update " + checkUpdate.get().getTimestamp());
                    }

                }
            }

        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {

                consumer.poll(Duration.ofMinutes(5));

            } catch (WakeupException e) {

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

            if (event.getTimestamp().isBefore(oldState.getTimestamp()) && event.getPayload().equals(oldState.getData())) {
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
