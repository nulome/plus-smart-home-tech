package grpc.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@GrpcService
@Slf4j
public class EventService extends CollectorControllerGrpc.CollectorControllerImplBase {
    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Вызов collectSensorEvent. Request: {}", request.getClass());
        super.collectSensorEvent(request, responseObserver);
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Вызов collectHubEvent. Request: {}", request.getClass());
        super.collectHubEvent(request, responseObserver);
    }
}
