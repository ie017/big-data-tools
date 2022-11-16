import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class JsonDeserializer implements Deserializer<String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<String> toString = String.class;

    public JsonDeserializer() {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        /**/
    }

    @Override
    public String deserialize(String s, byte[] bytes) {
        return new String();
    }

    @Override
    public String deserialize(String topic, Headers headers, byte[] data) {
        try {
            return this.objectMapper.readValue(data, toString);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void close() {
    }
}
