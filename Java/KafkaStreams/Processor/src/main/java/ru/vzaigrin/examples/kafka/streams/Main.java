package ru.vzaigrin.examples.kafka.streams;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaException;
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
import java.util.concurrent.ExecutionException;

public class Main {
    static Serde<Long> longSerde = Serdes.Long();
    static Serde<String> stringSerde = Serdes.String();
    static SpecificAvroSerde<UserProfile> userProfileSerde = new SpecificAvroSerde<>();
    static SpecificAvroSerde<PageView> pageViewSerde = new SpecificAvroSerde<>();
    static SpecificAvroSerde<PageViewWithRegion> pageViewWithRegionSerde = new SpecificAvroSerde<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage: Processor brokers schemaRegistryUrl");
            System.exit(-1);
        }

        String brokers = args[0];
        String registryUrl = args[1];
        String appId = "app4";
        String userProfilesTopic = "UserProfiles";
        String pageViewsTopic = "PageViews";
        String pageViewWithRegionTopic = "PageViewWithRegion";
        String internalTopic = appId + "_internal";

        Map<String, String> serdeConfig = Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryUrl);
        userProfileSerde.configure(serdeConfig, false);
        pageViewSerde.configure(serdeConfig, false);
        pageViewWithRegionSerde.configure(serdeConfig, false);

        Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, longSerde.getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, registryUrl);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/");

        // Создаём топик internalTopic, если такого нет
        AdminClient admin = AdminClient.create(props);
        try {
            if (!admin.listTopics().names().get().contains(internalTopic))
                admin.createTopics(Collections.singletonList(new NewTopic(internalTopic, 3, (short) 3))).all().get();
        }  catch (KafkaException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        // Хранилище для профилей пользователей
        StoreBuilder<KeyValueStore<Long, String>> userProfilesStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("userProfilesStore"),
                longSerde,
                stringSerde)
                .withLoggingDisabled();

        // Хранилище для агрегации просмотров страниц по регионам
        StoreBuilder<KeyValueStore<String, Long>> pageViewStore = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("pageViewStore"),
                stringSerde,
                longSerde)
                .withLoggingDisabled();

        Topology topology = new Topology();

        topology
                // Читаем топик с профилями пользователей и заполняем хранилище профилей
                .addSource(
                        "UserProfilesSource",
                        longSerde.deserializer(),
                        userProfileSerde.deserializer(),
                        userProfilesTopic)
                .addProcessor(
                        "userProfilesProcessor",
                        userProfilesProcessor::new,
                        "UserProfilesSource")
                // Читаем топик с просмотрами страниц и выводим во внутренний топик регион и страницу
                .addSource(
                        "PageViewsSource",
                        longSerde.deserializer(),
                        pageViewSerde.deserializer(),
                        pageViewsTopic)
                .addProcessor(
                        "PageViewWithRegionProcessor",
                        PageViewWithRegionProcessor::new,
                        "PageViewsSource")
                .addSink(
                        "RegionPageSink",
                        internalTopic,
                        stringSerde.serializer(),
                        stringSerde.serializer(),
                        "PageViewWithRegionProcessor")
                .addStateStore(
                        userProfilesStore,
                        "userProfilesProcessor", "PageViewWithRegionProcessor")
                // Читаем внутренний топик и заполняем хранилище просмотров страниц по регионам
                .addSource(
                        "RegionPageSource",
                        stringSerde.deserializer(),
                        stringSerde.deserializer(),
                        internalTopic)
                .addProcessor(
                        "PageViewsProcessor",
                        PageViewsProcessor::new,
                        "RegionPageSource")
                // Выводим просмотр страниц по регионам в выходной топик
                .addSink(
                        "PageViewWithRegionSink",
                        pageViewWithRegionTopic,
                        stringSerde.serializer(),
                        pageViewWithRegionSerde.serializer(),
                        "PageViewsProcessor")
                .addStateStore(
                        pageViewStore,
                        "PageViewsProcessor")
                ;

        System.out.println(topology.describe());

        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.cleanUp();
        streams.start();
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}