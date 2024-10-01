package grpc.service;

import collect.ControllerCollector;
import collect.mapper.HubEventMapper;
import collect.mapper.SensorEventMapper;
import collect.model.HubEvent;
import collect.model.SensorEvent;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.*;

@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {

    @Autowired
    private ControllerCollector controllerCollector;

    @Autowired
    private SensorEventMapper sensorEventMapper;

    @Autowired
    private HubEventMapper hubEventMapper;

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            SensorEvent sensorEvent = getPayloadCase(request);
            controllerCollector.collectSensorEvent(sensorEvent);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            HubEvent hubEvent = getPayloadCase(request);
            controllerCollector.collectHubEvent(hubEvent);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    private SensorEvent getPayloadCase(SensorEventProto event) {
        SensorEventProto.PayloadCase payloadCase = event.getPayloadCase();
        SensorEvent sensorEvent = null;
        switch (payloadCase) {
            case MOTION_SENSOR_EVENT:
                MotionSensorEvent motionSensorEvent = event.getMotionSensorEvent();
                sensorEvent = sensorEventMapper.toSensorEvent(motionSensorEvent);

                break;
            case TEMPERATURE_SENSOR_EVENT:
                TemperatureSensorEvent temperatureSensorEvent = event.getTemperatureSensorEvent();
                sensorEvent = sensorEventMapper.toSensorEvent(temperatureSensorEvent);
                break;
            case LIGHT_SENSOR_EVENT:
                LightSensorEvent lightSensor = event.getLightSensorEvent();
                sensorEvent = sensorEventMapper.toSensorEvent(lightSensor);
                break;
            case CLIMATE_SENSOR_EVENT:
                ClimateSensorEvent climateSensorEvent = event.getClimateSensorEvent();
                sensorEvent = sensorEventMapper.toSensorEvent(climateSensorEvent);
                break;
            case SWITCH_SENSOR_EVENT:
                SwitchSensorEvent switchSensorEvent = event.getSwitchSensorEvent();
                sensorEvent = sensorEventMapper.toSensorEvent(switchSensorEvent);
                break;

            default:
                System.out.println("Получено событие неизвестного типа: " + payloadCase);
        }
        sensorEvent.setId(event.getId());
        sensorEvent.setHubId(event.getHubId());
        return sensorEvent;
    }

    private HubEvent getPayloadCase(HubEventProto event) {
        HubEventProto.PayloadCase payloadCase = event.getPayloadCase();
        HubEvent hubEvent = null;
        switch (payloadCase) {
            case DEVICE_ADDED:
                DeviceAddedEventProto deviceAddedEventProto = event.getDeviceAdded();
                hubEvent = hubEventMapper.toHubEvent(deviceAddedEventProto);
                break;
            case DEVICE_REMOVED:
                DeviceRemovedEventProto deviceRemovedEventProto = event.getDeviceRemoved();
                hubEvent = hubEventMapper.toHubEvent(deviceRemovedEventProto);
                break;
            case SCENARIO_ADDED:
                ScenarioAddedEventProto scenarioAddedEventProto = event.getScenarioAdded();
                hubEvent = hubEventMapper.toHubEvent(scenarioAddedEventProto);
                break;
            case SCENARIO_REMOVED:
                ScenarioRemovedEventProto scenarioRemovedEventProto = event.getScenarioRemoved();
                hubEvent = hubEventMapper.toHubEvent(scenarioRemovedEventProto);
                break;

            default:
                System.out.println("Получено событие неизвестного типа: " + payloadCase);
        }

        hubEvent.setHubId(event.getHubId());
        return hubEvent;
    }

}

