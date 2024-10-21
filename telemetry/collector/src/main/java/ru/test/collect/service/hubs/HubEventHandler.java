package ru.test.collect.service.hubs;

import ru.test.collect.model.HubEvent;
import ru.test.collect.model.HubEventType;

public interface HubEventHandler {
    HubEventType getMessageType();

    void handle(HubEvent event);
}
