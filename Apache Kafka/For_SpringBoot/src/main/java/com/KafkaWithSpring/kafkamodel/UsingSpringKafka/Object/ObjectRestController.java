package com.KafkaWithSpring.kafkamodel.UsingSpringKafka.Object;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ObjectRestController {
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping(path = "/send/{topic}/{message}")
    public String sendObject(@PathVariable String topic, @PathVariable String message){
        //kafkaTemplate.send(topic, message);
        /* To send value with key */
        kafkaTemplate.send(topic, "key => "+message.length(), "value => "+message);
        return message;
    }
}
