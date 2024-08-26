package ru.vzaigrin.examples.kafka.streams;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String brokers = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
        String registryUrl = "http://127.0.0.1:8081";
        String appId = "app3";
        String userProfilesTopic = "UserProfiles";
        String pageViewsTopic = "PageViews";
        String pageViewWithRegionTopic = "PageViewWithRegion";

        Serde<Long> longSerde = Serdes.Long();
        Serde<String> stringSerde = Serdes.String();

        Map<String, String> serdeConfig = Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryUrl);

        SpecificAvroSerde<UserProfile> userProfileSerde = new SpecificAvroSerde<>();
        userProfileSerde.configure(serdeConfig, false);

        SpecificAvroSerde<PageView> pageViewSerde = new SpecificAvroSerde<>();
        pageViewSerde.configure(serdeConfig, false);

        SpecificAvroSerde<PageViewWithRegion> pageViewWithRegionSerde = new SpecificAvroSerde<>();
        pageViewWithRegionSerde.configure(serdeConfig, false);

        SpecificAvroSerde<PageRegion> pageRegionSerde = new SpecificAvroSerde<>();
        pageRegionSerde.configure(serdeConfig, false);

        Random random = new Random();

        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, longSerde.getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put("schema.registry.url", registryUrl);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/" + appId + random.nextInt(10));

        StreamsBuilder builder = new StreamsBuilder();
        KTable<Long, UserProfile> userProfileKTable = builder.table(userProfilesTopic, Consumed.with(longSerde, userProfileSerde));
        KStream<Long, PageView> pageViewKStream = builder.stream(pageViewsTopic, Consumed.with(longSerde, pageViewSerde));

        pageViewKStream
                .join(userProfileKTable,
                        (leftValue, rightValue) -> new PageRegion(rightValue.getRegion(), leftValue.getPage()))
                .groupBy((key, value) -> value.getRegion().toString(), Grouped.with(stringSerde, pageRegionSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .map((windowedKey, value) -> KeyValue.pair(windowedKey.key(), new PageViewWithRegion(windowedKey.key(), value)))
                .to(pageViewWithRegionTopic, Produced.with(stringSerde, pageViewWithRegionSerde))
        ;

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}