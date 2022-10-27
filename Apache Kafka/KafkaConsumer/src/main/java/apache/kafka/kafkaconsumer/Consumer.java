package apache.kafka.kafkaconsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Consumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        /* Pour spécifier le port de notre broker Kafka*/
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        /* ture signifié envoi les messages si l'intervalle demander est arrivé localhost:9092 */
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "true");
        /*Envoyer le message chaque 1s*/
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        /* Pour dire le consommateur est dans le groupe qui s'appelle groupe_1*/
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "Groupe_1");
        /* Le temps pour quitter la session = 1 min */
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 60000);
        /* Spécifiée le type de la clé*/
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        /* Spécifiée le type de la valeur*/
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        /*Créer un consumer qui admis l'objet properties, et qui connecter avec le broker et aussi il sait un certain
        * nombre des informations qui sont spécifient avec l'objet properties*/
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        /* Pour abonner à des topics */
        kafkaConsumer.subscribe(Collections.singletonList("topic1"));
        /* Pour dire je démarre un thread qui va entrer dans une boucle et qui fait un traitement spécifie dans chaque 1 seconde et avec un
        * décalage de 1 seconde */
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            /* Traitement*/
            System.out.println("*--------------------------*");
            /* Pour dire pendant chaque 1s faire un pool des messages qui ont été produits pendant les 100 ms derniers */
            ConsumerRecords<String, String> consumerRecord = kafkaConsumer.poll(Duration.ofMillis(100));
            /* Affichage des messages */
            consumerRecord.forEach(cr -> {
                System.out.println("Key =>"+ cr.key()+" value =>"+cr.value()+" offset =>" +cr.offset());
            });
        },1000, 1000, TimeUnit.MILLISECONDS);
    }
}
