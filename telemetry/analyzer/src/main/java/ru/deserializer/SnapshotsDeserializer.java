package ru.deserializer;


import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SnapshotsDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro> {
    public SnapshotsDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}
