package ru.vzaigrin.example.kafka.java;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.KafkaException;
import java.util.Properties;

public class Producer {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: Producer topic brokers");
            System.exit(-1);
        }

        // Параметры
        String brokers = args[1];
        String topic = args[0];

        // Создаём Producer
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<Integer, String> producer = new KafkaProducer<>(producerProps);

        // Генерируем записи
        try {
            for (int i = 1; i <= 1000; i++) {
                producer.send(new ProducerRecord<>(topic, i, String.format("Message %d", i)));
            }
        } catch (KafkaException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(-1);
        } finally {
            producer.flush();
            producer.close();
        }

        System.exit(0);
    }
}
