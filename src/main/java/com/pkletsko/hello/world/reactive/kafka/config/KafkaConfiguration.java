package com.pkletsko.hello.world.reactive.kafka.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;


@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaReceiver<String, com.pkletsko.integration.model.avro.UserInfoAvro> userInfoAvroKafkaReceiver(
            KafkaProperties kafkaProperties,
            ObjectProvider<SslBundles> sslBundles,
            @Value("${kafka.topic}") String topic
    ) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties(sslBundles.getIfAvailable());
        ReceiverOptions<String, com.pkletsko.integration.model.avro.UserInfoAvro> receiverOptions = ReceiverOptions.create(props);

        ReceiverOptions<String, com.pkletsko.integration.model.avro.UserInfoAvro> options = receiverOptions.subscription(Collections.singleton(topic))
                .commitInterval(Duration.ZERO)
                .commitBatchSize(0);
        return KafkaReceiver.create(options);
    }

    @Bean
    public KafkaSender<String, com.pkletsko.integration.model.avro.UserInfoAvro> userInfoAvroKafkaSender(
            KafkaProperties kafkaProperties,
            ObjectProvider<SslBundles> sslBundles
    ) {
        Map<String, Object> props = kafkaProperties.buildProducerProperties(sslBundles.getIfAvailable());
        var senderOptions = SenderOptions.<String, com.pkletsko.integration.model.avro.UserInfoAvro>create(props);
        return KafkaSender.create(senderOptions);
    }
}
