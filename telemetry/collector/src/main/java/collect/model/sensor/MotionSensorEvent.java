package collect.model.sensor;

import collect.model.SensorEvent;
import collect.model.SensorEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
    @NotNull
    private int linkQuality;
    @NotNull
    private boolean motion;
    @NotNull
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
