package ru.test.collect.service.sensor;

import ru.test.collect.model.SensorEvent;
import ru.test.collect.model.SensorEventType;
import ru.test.collect.model.sensor.ClimateSensorEvent;
import ru.test.kafka.CollectorTopics;
import ru.test.kafka.KafkaClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorEventHandler(KafkaClient kafkaClient, CollectorTopics topics) {
        super(kafkaClient, topics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent sensorEvent = (ClimateSensorEvent) event;

        return ClimateSensorAvro.newBuilder()
                .setCo2Level(sensorEvent.getCo2Level())
                .setTemperatureC(sensorEvent.getTemperatureC())
                .setHumidity(sensorEvent.getHumidity())
                .build();
    }

    @Override
    public void handle(SensorEvent event) {
        super.handle(event);
    }
}
