package ru.kafka.hubevent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HubEnetTopics {

    public final String topicHubs;

    public HubEnetTopics(@Value("${app.kafka.topic.hubs:telemetry.hubs.default}") String topicHubs) {
        this.topicHubs = topicHubs;
    }

}
