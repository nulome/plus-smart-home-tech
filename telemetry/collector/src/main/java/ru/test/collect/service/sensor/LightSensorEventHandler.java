package ru.test.collect.service.sensor;

import ru.test.collect.model.SensorEvent;
import ru.test.collect.model.SensorEventType;
import ru.test.collect.model.sensor.LightSensorEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {

    public LightSensorEventHandler(KafkaClient kafkaClient, CollectorTopics topics) {
        super(kafkaClient, topics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        LightSensorEvent sensorEvent = (LightSensorEvent) event;

        return LightSensorAvro.newBuilder()
                .setLinkQuality(sensorEvent.getLinkQuality())
                .setLuminosity(sensorEvent.getLuminosity())
                .build();
    }

    @Override
    public void handle(SensorEvent event) {
        super.handle(event);
    }
}
