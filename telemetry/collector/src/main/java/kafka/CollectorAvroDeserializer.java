package kafka;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class CollectorAvroDeserializer implements Deserializer<SpecificRecordBase> {
    private final DecoderFactory decoderFactory = DecoderFactory.get();

    @Override
    public SpecificRecordBase deserialize(String topic, byte[] bytes) {
        try {
            if (bytes != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(bytes, null);
                DatumReader<SpecificRecordBase> reader;

//                switch (topic) {
//                    default:
//                        throw new IllegalArgumentException("Неизвестный топик: " + topic);
//                }
//
//                return reader.read(null, decoder);
            }
            return null;
        } catch (Exception e) {
            throw new SerializationException("Ошибка десереализации данных из топика [" + topic + "]", e);
        }
    }
}
