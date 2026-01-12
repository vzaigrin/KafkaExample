package ru.example.kafka.transactions;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        String brokers = "localhost:9092";
        String topic2 = "topic2";
        String key = String.valueOf(ThreadLocalRandom.current().nextInt(100));
        String clientId = "app3_" + key;
        String transactionalId = "app3_" + key;

        System.out.printf("transactional.id = %s\n", transactionalId);

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            producer.initTransactions();
            producer.beginTransaction();

            for (int i = 1; i <= 10; i++) {
                producer.send(new ProducerRecord<>(topic2, key, String.format("%s: %d", clientId, i)));
                TimeUnit.SECONDS.sleep(1);
            }
            producer.flush();

            System.out.println("Press a key to finish");
            new Scanner(System.in).nextLine();
            producer.commitTransaction();

        } catch (KafkaException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}