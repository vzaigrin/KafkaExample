# Kafka Streams Word Count

Простое приложение для подсчёта слов на Kafka Streams

## Запуск

* Запускаем Kafka
* Создаём топик *input*
* Создаём топик *output*
* В первом терминале запускаем *java -jar KafkaStreamWordCount-assembly-1.0.jar*
* Во втором терминале запускаем *kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic output --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer*
* В третеьем терминале запускаем *kafka-console-producer.sh --bootstrap-server localhost:9092 --topic input*
