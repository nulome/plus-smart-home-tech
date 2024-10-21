package ru.kafka.snapshots;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SnapshotTopics {

    private final String topicSnapshots;

    public SnapshotTopics(@Value("${app.kafka.topic.snapshots:telemetry.snapshots.default}") String topicSnapshots) {
        this.topicSnapshots = topicSnapshots;
    }

}
