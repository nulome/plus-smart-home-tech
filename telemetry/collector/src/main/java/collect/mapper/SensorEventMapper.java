package collect.mapper;

import collect.model.SensorEvent;
import collect.model.sensor.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Mapper(componentModel = "spring")
public abstract class SensorEventMapper {

    @Mapping(target = "payload", expression = "java(this.createPayloadEvent(event))")
    public abstract SensorEventAvro toSensorEventAvro(SensorEvent event);

    public abstract LightSensorAvro toLightSensorAvro(LightSensorEvent event);

    public abstract SwitchSensorAvro toSwitchSensorAvro(SwitchSensorEvent event);

    public abstract ClimateSensorAvro toClimateSensorAvro(ClimateSensorEvent event);

    public abstract TemperatureSensorAvro toTemperatureSensorAvro(TemperatureSensorEvent event);

    public abstract MotionSensorAvro toMotionSensorAvro(MotionSensorEvent event);

    @Named("createPayloadEvent(event)")
    Object createPayloadEvent(SensorEvent event) {
        return switch (event.getType()) {
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightSensorEvent = (LightSensorEvent) event;
                yield this.toLightSensorAvro(lightSensorEvent);
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;
                yield this.toSwitchSensorAvro(switchSensorEvent);
            }
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;
                yield this.toClimateSensorAvro(climateSensorEvent);
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
                yield this.toTemperatureSensorAvro(temperatureSensorEvent);
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
                yield this.toMotionSensorAvro(motionSensorEvent);
            }
        };
    }
}
