package ru.kafka.snapshots;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnapshotTopics {

    public final String topicSnapshots;

    public SnapshotTopics(@Value("${app.kafka.topic.snapshots:telemetry.snapshots.default}") String topicSnapshots) {
        this.topicSnapshots = topicSnapshots;
    }

}
