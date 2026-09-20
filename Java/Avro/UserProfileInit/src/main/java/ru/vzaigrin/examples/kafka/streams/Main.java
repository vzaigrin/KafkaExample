package ru.vzaigrin.examples.kafka.streams;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.LongSerializer;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: UserProfileInit brokers registryURL");
            System.exit(-1);
        }

        // Параметры
        String brokers = args[0];
        String registryUrl = args[1];
        String clientId = "upp";
        String topic = "UserProfiles";

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put("schema.registry.url", registryUrl);

        try (KafkaProducer<Long, UserProfile> producer = new KafkaProducer<>(producerProps)) {
            Random random = new Random();
            for (int i = 1; i < 101; i++) {
                UserProfile userProfile = new UserProfile();
                userProfile.setUser("user" + i);
                userProfile.setRegion("region" + random.nextInt(10) + 1);
                if (ThreadLocalRandom.current().nextBoolean()) userProfile.setExperience("experience" + random.nextInt(5) + 1);
                else userProfile.setExperience(null);
                producer.send(new ProducerRecord<>(topic, (long) i, userProfile));
            }
        } catch (KafkaException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}