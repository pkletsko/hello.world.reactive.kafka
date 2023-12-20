package com.pkletsko.hello.world.reactive.kafka.service;

import com.pkletsko.integration.model.avro.UserInfoAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;

@Service
@Slf4j
public class UserInfoProducer {
    @Autowired
    private KafkaSender<String, com.pkletsko.integration.model.avro.UserInfoAvro> userInfoAvroKafkaSender;

    @Value("${kafka.topic}")
    String topic;

    public void sendUserInfo() {
        userInfoAvroKafkaSender
                .createOutbound()
                .send(buildOneMessage( createUserInfo())).then().subscribe();
    }
    private Mono<ProducerRecord<String, UserInfoAvro>> buildOneMessage(com.pkletsko.integration.model.avro.UserInfoAvro message) {
        return Mono.just(new ProducerRecord<>(
                topic,
                message
        ));
    }

    private static com.pkletsko.integration.model.avro.UserInfoAvro createUserInfo() {
        return com.pkletsko.integration.model.avro.UserInfoAvro.newBuilder()
                .setFirstName("MyFirst")
                .setLastName("MyLast")
                .setAge(125)
                .setPi(3.14)
                .setUniversityDegree(true)
                .build();
    }
}
