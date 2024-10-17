package ru.test.collect.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.test.collect.model.device.DeviceAction;
import ru.test.collect.model.device.DeviceAddedEvent;
import ru.test.collect.model.device.DeviceRemovedEvent;
import ru.test.collect.model.device.DeviceType;
import ru.test.collect.model.scenario.ScenarioAddedEvent;
import ru.test.collect.model.scenario.ScenarioCondition;
import ru.test.collect.model.scenario.ScenarioRemovedEvent;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Mapper(componentModel = "spring")
public abstract class HubEventMapper {

    public abstract ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition scenarioCondition);

    public abstract DeviceActionAvro toDeviceActionAvro(DeviceAction deviceAction);

    public abstract DeviceAddedEvent toHubEvent(DeviceAddedEventProto eventProto);

    public abstract DeviceRemovedEvent toHubEvent(DeviceRemovedEventProto eventProto);

    public abstract ScenarioAddedEvent toHubEvent(ScenarioAddedEventProto eventProto);

    public abstract ScenarioRemovedEvent toHubEvent(ScenarioRemovedEventProto eventProto);

    @Mapping(target = "type", expression = "java(this.getDeviceTypeAvro(addedEvent.getDeviceType()))")
    public abstract DeviceAddedEventAvro toDeviceAddedEventAvro(DeviceAddedEvent addedEvent);


    DeviceTypeAvro getDeviceTypeAvro(DeviceType type) {
        return DeviceTypeAvro.valueOf(type.name());
    }
}
