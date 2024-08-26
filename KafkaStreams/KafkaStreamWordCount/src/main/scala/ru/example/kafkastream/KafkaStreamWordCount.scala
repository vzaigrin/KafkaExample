package ru.example.kafkastream

import com.typesafe.config.ConfigFactory
import java.util.Properties
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.scala.serialization.Serdes._
import java.time.Duration.ofSeconds

object KafkaStreamWordCount {
  def main(args: Array[String]): Unit = {

    // Читаем конфигурационный файл
    val config           = ConfigFactory.load()
    val bootstrapServers = config.getString("bootstrap.servers")
    val inputTopic       = config.getString("input.topic")
    val outputTopic      = config.getString("output.topic")

    val props: Properties = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "WordCount")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)

    val builder: StreamsBuilder            = new StreamsBuilder
    val textLines: KStream[String, String] = builder.stream[String, String](inputTopic)

    val wordCounts: KTable[String, Long] = textLines
      .flatMapValues(textLine => textLine.toLowerCase.split("\\W+"))
      .groupBy((_, word) => word)
      .count()(Materialized.as("counts-store"))
    wordCounts.toStream.to(outputTopic)

    val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
    streams.start()

    sys.ShutdownHookThread {
      streams.close(ofSeconds(10))
    }
  }
}
