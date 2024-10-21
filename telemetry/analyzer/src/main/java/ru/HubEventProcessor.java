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
import ru.kafka.hubevent.HubEventTopics;
import ru.repository.ActionRepository;
import ru.repository.model.Action;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final Duration consumeAttemptTimeout;
    private final HubEventTopics topics;
    private final ActionRepository actionRepository;
    private final Consumer<String, SpecificRecordBase> consumer;

    public HubEventProcessor(@Value("${app.kafka.consume.attempt.timeout:10000}") Duration consumeAttemptTimeout,
                             HubEventTopics topics, KafkaClient hubEventKafka, ActionRepository actionRepository) {
        this.consumeAttemptTimeout = consumeAttemptTimeout;
        this.topics = topics;
        this.actionRepository = actionRepository;
        this.consumer = hubEventKafka.getConsumer();
    }

    @Override
    public void run() {

        try {
            consumer.subscribe(List.of(topics.getTopicHubs()));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(consumeAttemptTimeout);
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    log.info("topic = {}, offset = {}, value = {}", record.topic(), record.offset(), record.value());
                    HubEventAvro hubEventAvro = (HubEventAvro) record.value();
                    saveEvent(hubEventAvro);
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

    private void saveEvent(HubEventAvro hubEventAvro) {
        Action action = new Action();
        action.setType(hubEventAvro.getHubId());
        actionRepository.save(action);
    }

}
