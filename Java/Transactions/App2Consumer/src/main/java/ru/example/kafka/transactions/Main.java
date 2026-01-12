package ru.example.kafka.transactions;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String brokers = "localhost:9092";
        String groupId = "app2";
        String topic2 = "topic2";
        String topic3 = "topic3";

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Arrays.asList(topic2, topic3));

            while (true) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    if (records.count() > 0) {
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.printf("topic = %s, partition = %d, offset = %d, key = %s, value = %s%n",
                                    record.topic(), record.partition(), record.offset(), record.key(), record.value());
                        }
                    }
                } catch (KafkaException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } catch (KafkaException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
