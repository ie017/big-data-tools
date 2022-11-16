import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;


import java.io.IOException;
import java.util.Properties;

public class Streaming {
    public static void main(String[] args) throws JsonProcessingException {
        JsonSerde jsonSerde = new JsonSerde();
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streamer_ie017");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        properties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000");

        StreamsBuilder streamsBuilder = new StreamsBuilder();


        KStream<String, String> kStream = streamsBuilder.stream("topicie017", Consumed.with(Serdes.String(), Serdes.String()));
        /* On peut faire le traitement et après envoyer le flux de données */
        kStream.foreach((k,v) -> System.out.println(k+" , "+v));

        Topology topology = streamsBuilder.build();
        KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
        kafkaStreams.start();
    }

    /* Peut utiliser pour transfer value de type string to type Person*/
    private static Person toPerson(String v) throws JsonProcessingException{
        JsonMapper jsonMapper = new JsonMapper();
        Person person = null;
        try {
            person =  jsonMapper.readValue(v, Person.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return person;
    }
}
