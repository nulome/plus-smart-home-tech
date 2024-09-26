package collect;

import collect.model.HubEvent;
import collect.model.SensorEvent;

public interface ServiceCollector {
    void sendSensorKafka(SensorEvent event);

    void sendHubKafka(HubEvent event);
}
