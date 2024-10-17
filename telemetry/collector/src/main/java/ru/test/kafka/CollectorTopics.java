package ru.test.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CollectorTopics {
    public final String topicSensors;
    public final String topicHubs;

    public CollectorTopics(@Value("${app.kafka.topic.sensors:telemetry.sensors.default}") String topicSensors,
                           @Value("${app.kafka.topic.hubs:telemetry.hubs.default}") String topicHubs) {
        this.topicSensors = topicSensors;
        this.topicHubs = topicHubs;
    }
}
