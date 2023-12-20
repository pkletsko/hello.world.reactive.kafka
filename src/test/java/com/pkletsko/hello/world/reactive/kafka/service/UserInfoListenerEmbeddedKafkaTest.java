package com.pkletsko.hello.world.reactive.kafka.service;

import com.pkletsko.hello.world.reactive.kafka.config.KafkaEmbeddedTestConfiguration;
import com.pkletsko.hello.world.reactive.kafka.helper.KafkaTestProperties;
import com.pkletsko.integration.model.avro.UserInfoAvro;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        KafkaTestProperties.class,
        KafkaEmbeddedTestConfiguration.class,
        UserInfoListener.class

})
@EmbeddedKafka(partitions = 1)
public class UserInfoListenerEmbeddedKafkaTest {

    @Autowired
    private UserInfoListener userInfoListener;

    @Autowired
    private KafkaSender<String, com.pkletsko.integration.model.avro.UserInfoAvro> userInfoAvroKafkaSender;

    @Value("${kafka.topic}")
    private String topic;

    @Test
    public void first_test () {
        UserInfoAvro userInfo = createUserInfo();

        StepVerifier.create(
                userInfoAvroKafkaSender
                        .createOutbound()
                        .send(buildOneMessage(userInfo))
        ).verifyComplete();

        StepVerifier.create(userInfoListener.getUserInfoPublisher())
                .assertNext(entry -> assertThat(entry.value().getFirstName().toString()).isEqualTo(userInfo.getFirstName().toString()))
                .thenCancel()
                .verify(Duration.ofSeconds(7));
    }

    private Mono<ProducerRecord<String, com.pkletsko.integration.model.avro.UserInfoAvro>> buildOneMessage(com.pkletsko.integration.model.avro.UserInfoAvro message) {
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
