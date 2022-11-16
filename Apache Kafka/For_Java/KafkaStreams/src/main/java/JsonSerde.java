import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerde {
    /*JsonSerializer jsonSerializer = new JsonSerializer();
    JsonDeserializer jsonDeserializer = new JsonDeserializer();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serde.super.configure(configs, isKey);
    }

    @Override
    public void close() {
        Serde.super.close();
    }

    @Override
    public Serializer<Person> serializer() {
        return jsonSerializer;
    }

    @Override
    public Deserializer<String> deserializer() {
        return jsonDeserializer;
    }*/
}
