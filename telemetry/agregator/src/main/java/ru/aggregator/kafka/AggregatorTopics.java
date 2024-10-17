package ru.aggregator.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AggregatorTopics {

    public final String topicSensors;

    public final String topicSnapshots;

    public AggregatorTopics(@Value("${app.kafka.topic.sensors:telemetry.sensors.default}") String topicSensors,
                            @Value("${app.kafka.topic.snapshots:telemetry.snapshots.default}") String topicSnapshots) {
        this.topicSensors = topicSensors;
        this.topicSnapshots = topicSnapshots;
    }

}
