package ru.test;

import ru.test.collect.model.HubEvent;
import ru.test.collect.model.HubEventType;
import ru.test.collect.model.SensorEvent;
import ru.test.collect.model.SensorEventType;
import ru.test.collect.service.hubs.HubEventHandler;
import ru.test.collect.service.sensor.SensorEventHandler;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping("/events")
public class ControllerCollector {

    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public ControllerCollector(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Добавление в топик Kafka сообщение от Sensor: {}", event);
        SensorEventHandler handler = sensorEventHandlers.get(event.getType());
        handler.handle(event);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Добавление в топик Kafka сообщение от Device: {}", event);
        HubEventHandler handler = hubEventHandlers.get(event.getType());
        handler.handle(event);
    }

}
