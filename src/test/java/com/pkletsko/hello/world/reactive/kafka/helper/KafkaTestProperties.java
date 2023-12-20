package com.pkletsko.hello.world.reactive.kafka.helper;


import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@TestConfiguration
public class KafkaTestProperties {
    private static final AtomicInteger consumerCount = new AtomicInteger();

    private static final Map<String, Object> commonProperties = new HashMap<>(Map.of(
       "schema.registry.url",
       "testValue"
    ));

    private final Map<String, Object> consumerOnlyProperties = Map.of(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            MockKafkaAvroDeserializer.class,
            KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG,
            true
    );

    private static final Map<String, Object> producerOnlyConfig = new HashMap<>(Map.of(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            MockKafkaAvroSerializer.class
    ));

    public KafkaTestProperties(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ){
        commonProperties.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );
    }

    public Map<String, Object> buildConsumerConfig() {
        return buildConsumerConfig(String.format("test-consumer-%s", consumerCount.getAndIncrement()));
    }

    public Map<String, Object> buildConsumerConfig(String consumerGroup) {
        final var consumerConfig = new HashMap<>(commonProperties);
        consumerConfig.putAll(consumerOnlyProperties);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        return consumerConfig;
    }

    public Map<String, Object> buildProducerConfig() {
        final var producerConfig = new HashMap<>(commonProperties);
        producerConfig.putAll(producerOnlyConfig);
        return producerConfig;
    }
}
