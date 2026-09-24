package ru.vzaigrin.examples.kafka.streams;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: DSL brokers registryURL");
            System.exit(-1);
        }

        // Параметры
        String brokers = args[0];
        String registryUrl = args[1];
        String appId = "dsl";
        String userProfilesTopic = "UserProfiles";
        String pageViewsTopic = "PageViews";
        String pageViewWithRegionTopic = "PageViewWithRegion";

        Serde<Long> longSerde = Serdes.Long();
        Serde<String> stringSerde = Serdes.String();
        SpecificAvroSerde<UserProfile> userProfileSerde = new SpecificAvroSerde<>();
        SpecificAvroSerde<PageView> pageViewSerde = new SpecificAvroSerde<>();
        SpecificAvroSerde<PageViewWithRegion> pageViewWithRegionSerde = new SpecificAvroSerde<>();

        Map<String, String> serdeConfig = Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryUrl);
        userProfileSerde.configure(serdeConfig, false);
        pageViewSerde.configure(serdeConfig, false);
        pageViewWithRegionSerde.configure(serdeConfig, false);

        Random random = new Random();

        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, longSerde.getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryUrl);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/" + appId + random.nextInt(10));

        StreamsBuilder builder = new StreamsBuilder();
        KTable<Long, UserProfile> userProfileKTable = builder.table(userProfilesTopic, Consumed.with(longSerde, userProfileSerde));
        KStream<Long, PageView> pageViewKStream = builder.stream(pageViewsTopic, Consumed.with(longSerde, pageViewSerde));

        pageViewKStream
                .join(userProfileKTable,
                        (leftValue, rightValue) -> KeyValue.pair(rightValue.getRegion(), leftValue.getPage()))
                .map((key, value) -> KeyValue.pair(value.key.toString(), value.value.toString()))
                .groupByKey(Grouped.with(stringSerde, stringSerde))
                .count()
                .toStream()
                .map((key, value) -> KeyValue.pair(key, new PageViewWithRegion(key, value)))
                .to(pageViewWithRegionTopic, Produced.with(stringSerde, pageViewWithRegionSerde))
        ;

        Topology topology = builder.build();
        System.out.println(topology.describe());

        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}