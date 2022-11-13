package com.KafkaWithSpring.kafkamodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
/* Il faut injecter EnableKafka pour travailler avec Spring Kafka*/
@EnableKafka
public class KafkaModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaModelApplication.class, args);
    }

}
