package ru;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kafka.KafkaClient;
import ru.kafka.snapshots.SnapshotTopics;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {

    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(10000);

    private static final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Autowired
    private SnapshotTopics topics;

    @Autowired
    @Qualifier("snapshotKafka")
    private KafkaClient kafkaClient;

    @Override
    public void run() {
        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer();

        try {
            consumer.subscribe(List.of(topics.topicSnapshots));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    System.out.printf("topic = %s, offset = %d, value = %s%n", record.topic(), record.offset(), record.value());
                    SensorsSnapshotAvro snapshotAvro = (SensorsSnapshotAvro) record.value();
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
            }
        }
    }
}
