package apache.kafka.kafkaproducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Producer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "client-producer-0");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        Random random = new Random();
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(()->{
            String key = String.valueOf(random.nextInt(4000));
            String value = String.valueOf(random.nextInt(200));
            kafkaProducer.send(new ProducerRecord<String, String>("topic1", key, value), (recordMetadata, e) -> {
                System.out.println("message envoyer :"+value+" Partition :"+recordMetadata.partition());
            });
        },1000,100, TimeUnit.MILLISECONDS);
    }
}
