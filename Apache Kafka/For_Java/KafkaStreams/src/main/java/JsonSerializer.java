import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerializer implements Serializer<Person> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer() {
        //Nothing to do
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        /**/
    }

    @Override
    public byte[] serialize(String s, Person person) {
        return new byte[0];
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Person data) {
        try {
            if (data == null){
                System.out.println("Null received at serializing");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing MessageDto to byte[]");
        }
    }

    @Override
    public void close() {

    }
}