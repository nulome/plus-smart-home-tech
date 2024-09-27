package collect.mapper;


import collect.model.device.DeviceAction;
import collect.model.device.DeviceAddedEvent;
import collect.model.device.DeviceType;
import collect.model.scenario.ScenarioCondition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Mapper(componentModel = "spring")
public abstract class HubEventMapper {

    public abstract ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition scenarioCondition);

    public abstract DeviceActionAvro toDeviceActionAvro(DeviceAction deviceAction);

    @Mapping(target = "type", expression = "java(this.getDeviceTypeAvro(addedEvent.getDeviceType()))")
    public abstract DeviceAddedEventAvro toDeviceAddedEventAvro(DeviceAddedEvent addedEvent);


    DeviceTypeAvro getDeviceTypeAvro(DeviceType type) {
        return switch (type) {
            case CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
        };
    }
}
