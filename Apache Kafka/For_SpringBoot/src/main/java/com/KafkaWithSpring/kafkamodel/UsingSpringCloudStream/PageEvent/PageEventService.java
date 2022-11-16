package com.KafkaWithSpring.kafkamodel.UsingSpringCloudStream.PageEvent;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class PageEventService {
    //@Bean
    /*Pour notre app reçoit un flux en entrée comme un consommateur, et pour produire l'objet PageEvent
    * on a deux maniérés soit par spécifié la valeur dans le terminal avec la commande :
    * .\bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9092 --topic mytopic, soit via
    * le web par l'utilisation de Rest API */
    public Consumer<PageEvent> PageEventConcumer(){
        return (input)->{
            System.out.println("----------------------");
            System.out.println(input.toString());
            System.out.println("----------------------");
        };
    }
    //@Bean
    /* Cette methode permet notre app de générer un objet de type PageEvent et l'envoi le flux (repetition) en sortie,
    * et pour la consummation de ce flux-là il suffit juste spécifie un consommateur dans la terminale via la commande :
    * .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic mytopic-out*/
    public Supplier<PageEvent> PageEventSupplier(){
        return ()-> new PageEvent(Math.random()>0.5? "ie017":"-NUN-", "ie017", new Date(), new Random().nextInt(99));
    }
    //@Bean
    /* Cette methode permet notre application de prendre un flux en entrée et fait les traitements, après il envoie le flux produit via la sortie*/
    /* Si le consumer est connecté avec le meme topic qui reçoit le message de producer alors le consumer va récupérer 2 messages
     (l'objet envoyé et l'objet traiter), sinon il récupère seulement le message traiter */
    public Function<PageEvent, String> PageEventFunction(){
        return (input)->{
            input.setName("has changed");
            input.setUser("ie017");
            return input.toString();
        };
    }
    // @Bean
    /*KStream avec la clé de type string et la valeur de type PageEvent*/
    public Function<KStream<String, PageEvent>, KStream<String, Long>> KStreamFunction(){
        return (input)->{
            return input.filter((k,v)-> v.getDuration()>40).
                    map((k,v)-> new KeyValue<>(v.getName(), 0L)).
                    groupBy((k,v)-> k,Grouped.with(Serdes.String(), Serdes.Long())).
                    windowedBy(TimeWindows.of(Duration.ofSeconds(5)))./*C'est-à-dire l'opération de count() doit fait uniquement sur
                    les ensembles des enregistrements qui ont été observées pendant les 5 dernières secondes */
                    count(Materialized.as("page-count"))./*Une fois kafka fais le calcule il produit une table qui s'appel page-count*/
                    toStream().
                    map((k,v)-> new KeyValue<>("=>"+k.window().startTime()+k.window().endTime()+k.key(), v));
        };
    }
}
