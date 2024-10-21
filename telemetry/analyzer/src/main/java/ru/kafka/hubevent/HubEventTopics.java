package ru.kafka.hubevent;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class HubEventTopics {

    private final String topicHubs;

    public HubEventTopics(@Value("${app.kafka.topic.hubs:telemetry.hubs.default}") String topicHubs) {
        this.topicHubs = topicHubs;
    }

}
