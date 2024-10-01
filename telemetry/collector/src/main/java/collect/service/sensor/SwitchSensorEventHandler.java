package collect.service.sensor;

import collect.model.SensorEvent;
import collect.model.SensorEventType;
import collect.model.sensor.SwitchSensorEvent;
import kafka.CollectorTopics;
import kafka.KafkaClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Service
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaClient kafkaClient, CollectorTopics topics) {
        super(kafkaClient, topics);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent sensorEvent = (SwitchSensorEvent) event;

        return SwitchSensorAvro.newBuilder()
                .setState(sensorEvent.isState())
                .build();
    }

    @Override
    public void handle(SensorEvent event) {
        super.handle(event);
    }
}
