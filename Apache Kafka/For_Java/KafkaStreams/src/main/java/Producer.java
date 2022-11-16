import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Producer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "client-producer-0");

        KafkaProducer<String, Person> kafkaProducer = new KafkaProducer<String, Person>(properties);
        Random random = new Random();
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            String key = UUID.randomUUID().toString();
            String getUniqueId = String.format("%s_%s", UUID.randomUUID().toString().substring(0, 5), System.currentTimeMillis() / 1000);

            Person value = new Person(key, "ie0"+String.valueOf(random.nextInt(99)) , String.format("%s@%s", getUniqueId, "ie017.ma"),
                    random.nextInt(9)< 5 ? Gender.FEMALE : Gender.MALE, new Date());
            kafkaProducer.send(new ProducerRecord<String, Person>("topicie017", key, value), (recordMetadata, e) -> {
                System.out.println("message envoyer :"+value+" Partition :"+recordMetadata.partition());
            });
        },1000,1000, TimeUnit.MILLISECONDS);
    }
}
