package ru.aggregator.kafka;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AggregatorTopics {

    private final String topicSensors;

    private final String topicSnapshots;

    public AggregatorTopics(@Value("${app.kafka.topic.sensors:telemetry.sensors.default}") String topicSensors,
                            @Value("${app.kafka.topic.snapshots:telemetry.snapshots.default}") String topicSnapshots) {
        this.topicSensors = topicSensors;
        this.topicSnapshots = topicSnapshots;
    }

}
