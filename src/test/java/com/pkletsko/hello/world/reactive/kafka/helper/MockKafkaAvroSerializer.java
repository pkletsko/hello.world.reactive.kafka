package com.pkletsko.hello.world.reactive.kafka.helper;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.SneakyThrows;
import org.apache.avro.Schema;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

public class MockKafkaAvroSerializer extends KafkaAvroSerializer {
    public MockKafkaAvroSerializer() {super(new MockSchemaRegistryClient());}

    public MockKafkaAvroSerializer(SchemaRegistryClient registryClient) {super(new MockSchemaRegistryClient());}

    public MockKafkaAvroSerializer(SchemaRegistryClient registryClient, Map<String, ?> properties) {
        super(
                new MockSchemaRegistryClient(),
                properties
        );
    }
}
