package collect.service.sensor;

import collect.model.SensorEvent;
import collect.model.SensorEventType;
import collect.model.sensor.LightSensorEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Service
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
