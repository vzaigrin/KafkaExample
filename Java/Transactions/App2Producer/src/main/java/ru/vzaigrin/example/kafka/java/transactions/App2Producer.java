package ru.vzaigrin.example.kafka.java.transactions;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class App2Producer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: App2Producer brokers");
            System.exit(-1);
        }

        // Параметры
        String brokers = args[0];
        String clientId = "app2";
        String transactionalId = "app2";
        String topic2 = "topic2";
        String topic3 = "topic3";

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            producer.initTransactions();

            producer.beginTransaction();
            for (int i = 1; i <= 5; i++) {
                producer.send(new ProducerRecord<>(topic2, String.format("%d", i), String.format("1-%d", i)));
                producer.send(new ProducerRecord<>(topic3, String.format("%d", i), String.format("1-%d", i)));
            }
            producer.flush();
            producer.commitTransaction();

            TimeUnit.SECONDS.sleep(10);
            producer.beginTransaction();
            for (int i = 1; i <= 5; i++) {
                producer.send(new ProducerRecord<>(topic2, String.format("%d", i), String.format("2-%d", i)));
                producer.send(new ProducerRecord<>(topic3, String.format("%d", i), String.format("2-%d", i)));
            }
            producer.flush();
            producer.abortTransaction();

        } catch (KafkaException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
        System.exit(0);
    }
}