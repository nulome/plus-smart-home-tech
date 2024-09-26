package collect;

import collect.model.HubEvent;
import collect.model.SensorEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
public class ControllerCollector {

    private final ServiceCollector serviceCollector;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Добавление в топик Kafka сообщение от Sensor: {}", event);
        serviceCollector.sendSensorKafka(event);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Добавление в топик Kafka сообщение от Device: {}", event);
        serviceCollector.sendHubKafka(event);
    }

}
