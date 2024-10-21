package ru.test.collect.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.test.collect.model.sensor.ClimateSensorEvent;
import ru.test.collect.model.sensor.LightSensorEvent;
import ru.test.collect.model.sensor.MotionSensorEvent;
import ru.test.collect.model.sensor.SwitchSensorEvent;
import ru.test.collect.model.sensor.TemperatureSensorEvent;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = SensorEventType.class
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = LightSensorEvent.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = ClimateSensorEvent.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = MotionSensorEvent.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureSensorEvent.class, name = "TEMPERATURE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchSensorEvent.class, name = "SWITCH_SENSOR_EVENT")
})

@Setter
@Getter
@ToString
public abstract class SensorEvent {
    @NotNull
    private String id;
    @NotNull
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract SensorEventType getType();
}
