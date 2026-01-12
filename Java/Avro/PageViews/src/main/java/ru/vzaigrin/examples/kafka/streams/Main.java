package ru.vzaigrin.examples.kafka.streams;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.KafkaException;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String brokers = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
        String registryUrl = "http://127.0.0.1:8081";
        String clientId = "pvp";
        String topic = "PageViews";

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put("schema.registry.url", registryUrl);

        try (KafkaProducer<Long, PageView> producer = new KafkaProducer<>(producerProps)) {
            while(true) {
                Random random = new Random();
                Long id = (long) (random.nextInt(100) + 1);

                PageView pageView = new PageView();
                pageView.setUser("user" + id);
                pageView.setPage("page" + random.nextInt(500) + 1);
                pageView.setIndustry("industry" + random.nextInt(10) + 1);

                if (ThreadLocalRandom.current().nextBoolean()) pageView.setFlags("flag");
                else pageView.setFlags(null);

                producer.send(new ProducerRecord<>(topic, id, pageView));
                TimeUnit.SECONDS.sleep(10);
            }
        } catch (KafkaException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}