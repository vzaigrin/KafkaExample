package ru.vzaigrin.examples.kafka.streams;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.KafkaException;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException {
        String brokers = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
        String registryUrl = "http://127.0.0.1:8081";
        String clientId = "upp";
        String topic = "UserProfiles";

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put("schema.registry.url", registryUrl);

        try (KafkaProducer<Long, GenericRecord> producer = new KafkaProducer<>(producerProps)) {
            Schema schema = new Schema.Parser().parse(new File("src/main/resources/UserProfile.avsc"));
            Random random = new Random();

            while(true) {
                Long id = (long) (random.nextInt(100) + 1);

                GenericRecord record = new GenericData.Record(schema);
                record.put("user", "user" + id);
                record.put("region", "region" + random.nextInt(10) + 1);

                if (ThreadLocalRandom.current().nextBoolean()) record.put("experience", "experience" + random.nextInt(5) + 1);
                else record.put("experience", null);

                producer.send(new ProducerRecord<>(topic, id, record));
                TimeUnit.MINUTES.sleep(5);
            }
        } catch (KafkaException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}