package com.KafkaWithSpring.kafkamodel.UsingSpringCloudStream.PageEvent;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@RestController
@RequestMapping(path = "/event")
public class PageEventRestController {
    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    private InteractiveQueryService interactiveQueryService; /*Objet utilisé pour interroger le store ou la table qui s'appel page-count et qui se trouve dans PageEventService classe */
    @GetMapping(path = "/publish/{topic}/{name}")
    public PageEvent publish(@PathVariable String topic, @PathVariable String name){
        PageEvent pageEvent = new PageEvent(name, "ie017", new Date(), new Random().nextInt(99));
        streamBridge.send(topic, pageEvent);
        return pageEvent;
        /*On commencera à faire un test avec le topic mytopic qui était deja créer,
        * On peut spécifier les parameters des kafka dans application.properties
        * sinon spring concéder les parameters par défaut comme le port 9092*/

        /*Le résultat d'exécution de lien http://localhost:8080/event/publish/mytopic/ahmed est par exemple :
        * {
             "name": "ahmed",
             "user": "ie017",
             "date": "2022-09-11T19:41:07.331+00:00",
             "duration": 96
          }
          * est cet objet-là était envoyé vers des consommateurs qui sont abonnées à le topic mytopic
          * , et lorsqu'on affiche ce message-là via la console cmd en trouve le résultat suivant :
          * {"name":"ahmed","user":"ie017","date":"2022-09-11T19:41:07.331+00:00","duration":96} */
    }
    @GetMapping(path = "/analytics", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Long>> analytics(){ /*Flux est un flux qui continue un objet Map qui doit etre converter en format json*/
        return Flux.interval(Duration.ofSeconds(1))./*Pour générer un flux d'enregistrement chaque seconde*/
                map(sequence->{
                    /*Pour chaque seconde je crée un objet de type HashMap*/
            Map<String, Long> stringLongMap = new HashMap<>();
            ReadOnlyWindowStore<String, Long> windowStore = interactiveQueryService.getQueryableStore("page-count", QueryableStoreTypes.windowStore());/* Pour interroger le store qui s'appel page-count chaque 1s et de type windowStore
            car dans PageEventService on a deja la table page-count générer par windowedBy*/
            Instant now = Instant.now(); /*Pour get la date actuelle ex : 21.30.00*/
            Instant from = now.minusMillis(5000);/*Pour get la date before le 5s de la date now ex : 21.29.55*/
            KeyValueIterator<Windowed<String>, Long> fetchAll = windowStore.fetchAll(from, now); /* Faire un fetch pondant la dernière 5s, et obtenir le résultat
            se forme d'un iterator qui contient l'objet windowed avec la clé de type string et une valeur de type Long*/
            //WindowStoreIterator<Long> fetch = stats.fetch(name, from, now);
            while(fetchAll.hasNext()){
                KeyValue<Windowed<String>, Long> next = fetchAll.next();/* Obtenir l'objet */
                stringLongMap.put(next.key.key(), next.value); /*Put la valeur et la clé de l'objet obtenue, et le
                stocker dans stringLongMap*/
            }
            return stringLongMap;
        }).share();/*Share signifiée qui le flux obtenu est partagée par plusieurs users*/
    }
}
