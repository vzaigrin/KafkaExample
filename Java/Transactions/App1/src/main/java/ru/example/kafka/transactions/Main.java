package ru.example.kafka.transactions;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.InvalidProducerEpochException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.*;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        String brokers = "localhost:9092";
        String groupId = "app1";
        String clientId = "app1";
        String transactionalId = "app1";
        String topic1 = "topic1";
        String topic2 = "topic2";
        String topic3 = "topic3";

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
        producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
             KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {

            producer.initTransactions();
            consumer.subscribe(Collections.singletonList(topic1));

            while (true) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
                    if (records.count() > 0) {
                        producer.beginTransaction();
                        for (ConsumerRecord<String, String> record : records) {
                            String key = record.key();
                            String value = record.value();

                            offsets.clear();
                            offsets.put(new TopicPartition(record.topic(), record.partition()),
                                    new OffsetAndMetadata(record.offset() + 1));

                            producer.send(new ProducerRecord<>(topic2, key, value));
                            producer.send(new ProducerRecord<>(topic3, key, value));
                            producer.sendOffsetsToTransaction(offsets, consumer.groupMetadata());
                        }

                        if (ThreadLocalRandom.current().nextBoolean()) producer.commitTransaction();
                        else producer.abortTransaction();
                    }
                } catch (ProducerFencedException | InvalidProducerEpochException e) {
                    producer.abortTransaction();
                    throw new KafkaException(String.format("transactional.id %s используется другим приложением", transactionalId));
                }
            }
        } catch (KafkaException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}