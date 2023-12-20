package com.pkletsko.hello.world.reactive.kafka.config;

import com.pkletsko.hello.world.reactive.kafka.helper.KafkaTestProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;

@TestConfiguration
@EnableKafka
public class KafkaEmbeddedTestConfiguration {

    @Bean
    public KafkaSender<String, com.pkletsko.integration.model.avro.UserInfoAvro> kafkaUserInfoSender(
            KafkaTestProperties kafkaTestProperties
    ) {
        var senderOptions = SenderOptions.<String, com.pkletsko.integration.model.avro.UserInfoAvro>create(kafkaTestProperties.buildProducerConfig());
        return KafkaSender.create(senderOptions);
    }

    @Bean
    public KafkaReceiver<String, com.pkletsko.integration.model.avro.UserInfoAvro> kafkaUserInfoReceiver(
            KafkaTestProperties kafkaTestProperties,
            @Value("${kafka.topic}") String topic
    ) {
        var receiverOptions = ReceiverOptions.<String, com.pkletsko.integration.model.avro.UserInfoAvro>create(kafkaTestProperties.buildConsumerConfig())
                .subscription(Collections.singleton(topic));

        return KafkaReceiver.create(receiverOptions);
    }
}
