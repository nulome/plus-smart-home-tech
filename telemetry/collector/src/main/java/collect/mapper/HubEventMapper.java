package collect.mapper;


import collect.model.HubEvent;
import collect.model.device.DeviceAction;
import collect.model.device.DeviceAddedEvent;
import collect.model.device.DeviceRemovedEvent;
import collect.model.device.DeviceType;
import collect.model.scenario.ScenarioAddedEvent;
import collect.model.scenario.ScenarioCondition;
import collect.model.scenario.ScenarioRemovedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class HubEventMapper {

    public abstract ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition scenarioCondition);

    public abstract DeviceActionAvro toDeviceActionAvro(DeviceAction deviceAction);


    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "payload", expression = "java(this.createPayloadEvent(event))")
    public abstract HubEventAvro toHubEventAvro(HubEvent event);

    @Named("createPayloadEvent(event)")
    Object createPayloadEvent(HubEvent event) {
        return switch (event.getType()) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent addedEvent = (DeviceAddedEvent) event;
                yield DeviceAddedEventAvro.newBuilder()
                        .setId(addedEvent.getId())
                        .setType(getDeviceTypeAvro(addedEvent.getDeviceType()))
                        .build();
            }
            case DEVICE_REMOVED -> {
                DeviceRemovedEvent removedEvent = (DeviceRemovedEvent) event;
                yield DeviceRemovedEventAvro.newBuilder()
                        .setId(removedEvent.getId())
                        .build();
            }
            case SCENARIO_ADDED -> {
                ScenarioAddedEvent addedEvent = (ScenarioAddedEvent) event;
                yield ScenarioAddedEventAvro.newBuilder()
                        .setName(addedEvent.getName())
                        .setActions(addedEvent.getActions().stream()
                                .map(this::toDeviceActionAvro)
                                .collect(Collectors.toList()))
                        .setConditions(addedEvent.getConditions().stream()
                                .map(this::toScenarioConditionAvro)
                                .collect(Collectors.toList()))
                        .build();
            }
            case SCENARIO_REMOVED -> {
                ScenarioRemovedEvent removedEvent = (ScenarioRemovedEvent) event;
                yield ScenarioRemovedEventAvro.newBuilder()
                        .setName(removedEvent.getName())
                        .build();
            }
        };
    }

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
