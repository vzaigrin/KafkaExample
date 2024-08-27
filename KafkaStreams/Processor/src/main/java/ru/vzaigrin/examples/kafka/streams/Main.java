package ru.vzaigrin.examples.kafka.streams;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Main {
    static Serde<Long> longSerde = Serdes.Long();
    static Serde<String> stringSerde = Serdes.String();
    static SpecificAvroSerde<UserProfile> userProfileSerde = new SpecificAvroSerde<>();
    static SpecificAvroSerde<PageView> pageViewSerde = new SpecificAvroSerde<>();
    static SpecificAvroSerde<PageViewWithRegion> pageViewWithRegionSerde = new SpecificAvroSerde<>();

    public static void main(String[] args) {
        String brokers = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
        String registryUrl = "http://127.0.0.1:8081";
        String appId = "processor";
        String userProfilesTopic = "UserProfiles";
        String pageViewsTopic = "PageViews";
        String pageViewWithRegionTopic = "PageViewWithRegion";

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
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put("schema.registry.url", registryUrl);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/" + appId + random.nextInt(10));

        StoreBuilder<KeyValueStore<Long, String>> userProfilesStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("userProfilesStore"),
                longSerde,
                stringSerde);

        StoreBuilder<KeyValueStore<String, Long>> pageViewStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("pageViewStore"),
                stringSerde,
                longSerde);

        Topology topology = new Topology();

        topology
                .addSource("UserProfilesSource", longSerde.deserializer(), userProfileSerde.deserializer(), userProfilesTopic)
                .addProcessor("userProfilesProcessor", userProfilesProcessor::new, "UserProfilesSource")
                .addSource("PageViewsSource", longSerde.deserializer(), userProfileSerde.deserializer(), pageViewsTopic)
                .addProcessor("PageViewsProcessor", PageViewsProcessor::new, "PageViewsSource")
                .addStateStore(userProfilesStore, "userProfilesProcessor", "PageViewsProcessor")
                .addStateStore(pageViewStore, "PageViewsProcessor")
                .addSink("PageViewWithRegionSink", pageViewWithRegionTopic, stringSerde.serializer(), pageViewWithRegionSerde.serializer(), "PageViewsProcessor")
                ;

        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();
    }
}