package ru.test.collect.mapper;


import org.mapstruct.Mapper;
import ru.test.collect.model.sensor.ClimateSensorEvent;
import ru.test.collect.model.sensor.LightSensorEvent;
import ru.test.collect.model.sensor.MotionSensorEvent;
import ru.test.collect.model.sensor.SwitchSensorEvent;
import ru.test.collect.model.sensor.TemperatureSensorEvent;

@Mapper(componentModel = "spring")
public abstract class SensorEventMapper {

    public abstract ClimateSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.MotionSensorEvent event);

    public abstract LightSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.LightSensorEvent event);

    public abstract MotionSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.ClimateSensorEvent event);

    public abstract SwitchSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.SwitchSensorEvent event);

    public abstract TemperatureSensorEvent toSensorEvent(ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorEvent event);

}
