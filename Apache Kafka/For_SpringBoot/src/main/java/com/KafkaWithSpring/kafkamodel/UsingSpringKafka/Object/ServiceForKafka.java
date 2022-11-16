package com.KafkaWithSpring.kafkamodel.UsingSpringKafka.Object;

import com.KafkaWithSpring.kafkamodel.UsingSpringCloudStream.PageEvent.PageEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ServiceForKafka {
    @KafkaListener(topics = {"topicie017"}, groupId = "groupe-topic")
    public void flux(ConsumerRecord<Integer, String> myflux) throws JsonProcessingException {
        PageEvent pageEvent = pageEvent(myflux.value());
        /* affichage de flux des messages, producer envoi le message et le consumer reçoit le message après 1s (durée par défaut) */
        System.out.println("----------------------");
        System.out.println("key => "+myflux.key()+", Name => "+pageEvent.getName()
                +", User => "+pageEvent.getUser()+", Duration => "+pageEvent.getDuration());
        System.out.println("----------------------");
    }

    /* Utiliser cette configuration pour récupérer les objets de type String est le transfer via le topic topicie017 avec le format json*/
    private PageEvent pageEvent(String flux) throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        PageEvent pageEvent = jsonMapper.readValue(flux, PageEvent.class);
        return pageEvent;
    }

    /* test : dans cette situation j'envoie l'élément de type string : {"name": "issam","user": "ie017","date": "2022-11-13T12:23:40.225+00:00","duration": 40}
    * et comme reponse je reçoi key => null, Name => issam, User => ie017, Duration => 40 dans le terminal et l'objet de type json dans le cmd : consumer
    * {"name": "issam","user": "ie017","date": "2022-11-13T12:23:40.225+00:00","duration": 40} */
}
