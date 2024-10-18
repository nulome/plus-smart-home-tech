package ru.kafka.hubevent;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kafka.KafkaClient;

import java.util.Properties;

@Configuration
public class HubEventKafkaClientConfig {

    private final String kafkaUrlServer;

    public HubEventKafkaClientConfig(@Value("${app.kafka.servers:localhost:9099}") String kafkaUrlServer) {
        this.kafkaUrlServer = kafkaUrlServer;
    }


    @Bean("hubEventKafka")
    @Qualifier("hubEventKafka")
    KafkaClient getKafkaClient() {
        return new KafkaClient() {

            private Consumer<String, SpecificRecordBase> consumer;

            private Producer<String, SpecificRecordBase> producer;

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrlServer);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

                producer = new KafkaProducer<>(config);
            }

            @Override
            public Consumer<String, SpecificRecordBase> getConsumer() {
                if (consumer == null) {
                    initConsumer();
                }
                return consumer;
            }

            private void initConsumer() {
                Properties config = new Properties();
                config.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrlServer);
                config.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
                config.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "ru.deserializer.HubEventDeserializer");
                config.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "consumer-hubevent-1");

                consumer = new KafkaConsumer<>(config);
            }

            @Override
            public void stop() {
                if (consumer != null) {
                    consumer.close();
                }

                if (producer != null) {
                    producer.close();
                }
            }
        };
    }

}
