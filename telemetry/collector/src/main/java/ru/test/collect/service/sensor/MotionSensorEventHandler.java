package ru.test.collect.service.sensor;

import ru.test.collect.model.SensorEvent;
import ru.test.collect.model.SensorEventType;
import ru.test.collect.model.sensor.MotionSensorEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(KafkaClient kafkaClient, CollectorTopics topics) {
        super(kafkaClient, topics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent event) {
        MotionSensorEvent sensorEvent = (MotionSensorEvent) event;

        return MotionSensorAvro.newBuilder()
                .setLinkQuality(sensorEvent.getLinkQuality())
                .setMotion(sensorEvent.isMotion())
                .setVoltage(sensorEvent.getVoltage())
                .build();
    }

    @Override
    public void handle(SensorEvent event) {
        super.handle(event);
    }
}
