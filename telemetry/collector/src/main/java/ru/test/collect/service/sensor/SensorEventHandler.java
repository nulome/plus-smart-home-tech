package ru.test.collect.service.sensor;

import ru.test.collect.model.SensorEvent;
import ru.test.collect.model.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
