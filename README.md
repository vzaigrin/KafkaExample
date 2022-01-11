# Kafka: запись и чтение данных

## Постановка задачи

Дан [CSV файл](https://www.kaggle.com/sootersaalu/amazon-top-50-bestselling-books-2009-2019)

Необходимо:
1. Создать в Kafka тему *books* с тремя секциями (partition).
2. Написать **Producer**, который будет читать файл, сериализовать его в JSON и отправлять в тему *books*.
3. Написать **Consumer**, который будет читать данные из темы *books* и выводить в *stdout* последние 5 записей (с максимальным значением *offset*) из каждой секции. При чтении темы одновременно можно хранить в памяти только 15 записей.

## Реализация

* *KafkaAPI* - решение на стандартном [Kafka API](http://kafka.apache.org/documentation/#api).
* *AlpakkaKafka* - решение на [Akka Streams](https://doc.akka.io/docs/akka/current/stream/index.html) и [Alpakka Kafka](https://doc.akka.io/docs/alpakka-kafka/current/index.html).
* *StructuredStreaming* - решение на [Spark Structured Streaming](http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html).
* *ZIOKafka* - решение построено на [ZIO Streams](https://zio.dev/version-1.x/datatypes/stream/) и [ZIO Kafka](https://github.com/zio/zio-kafka).
