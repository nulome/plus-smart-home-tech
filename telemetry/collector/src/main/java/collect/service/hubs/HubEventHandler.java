package collect.service.hubs;

import collect.model.HubEvent;
import collect.model.HubEventType;

public interface HubEventHandler {
    HubEventType getMessageType();

    void handle(HubEvent event);
}
