package collect.mapper;


import collect.model.SensorEvent;
import collect.model.sensor.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Mapper(componentModel = "spring")
public abstract class SensorEventMapper {

    public abstract ClimateSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.MotionSensorEvent event);

    public abstract LightSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.LightSensorEvent event);

    public abstract MotionSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.ClimateSensorEvent event);

    public abstract SwitchSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.SwitchSensorEvent event);

    public abstract TemperatureSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorEvent event);

}
