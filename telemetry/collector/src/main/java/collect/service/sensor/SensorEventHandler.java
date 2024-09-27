package collect.service.sensor;

import collect.model.SensorEvent;
import collect.model.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
