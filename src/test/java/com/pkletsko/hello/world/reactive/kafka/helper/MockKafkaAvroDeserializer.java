package com.pkletsko.hello.world.reactive.kafka.helper;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.SneakyThrows;
import org.apache.avro.Schema;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

public class MockKafkaAvroDeserializer extends KafkaAvroDeserializer {
    public MockKafkaAvroDeserializer() {super(createMockClient());}

    public MockKafkaAvroDeserializer(SchemaRegistryClient registryClient) {super(createMockClient());}

    public MockKafkaAvroDeserializer(SchemaRegistryClient registryClient, Map<String, ?> properties) {
        super(
                createMockClient(),
                properties
        );
    }

    @SneakyThrows
    private static SchemaRegistryClient createMockClient() {
        var client = new MockSchemaRegistryClient();
        client.register("user-info-kafka-topic", getSchema("schema/UserInfoUpdateMessage_v1.avsc"));
        return client;
    }
    @SneakyThrows
    private static Schema getSchema(final String pathToSchema) {
        return new Schema.Parser().parse(new ClassPathResource(pathToSchema).getInputStream());
    }
}
