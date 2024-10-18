package ru.test.collect.model.device;


import ru.test.collect.model.HubEvent;
import ru.test.collect.model.HubEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceRemovedEvent extends HubEvent {

    @NotNull
    private String id;
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
