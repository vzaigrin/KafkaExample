package ru.example.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Duration
import java.util.Properties

object KafkaPerfTest {
  def main(args: Array[String]): Unit = {
    if (args.length != 3) {
      println("Usage: KafkaPerfTest brokers topic messageCount")
      sys.exit(-1)
    }

    val brokers      = args(0)
    val topic        = args(1)
    val messageCount = args(2).toInt

    // Создаём Producer
    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    val producer = new KafkaProducer(props, new StringSerializer, new StringSerializer)

    // Отправляем в Kafka
    val start = System.nanoTime

    (0 until messageCount) foreach { _ =>
      producer.send(new ProducerRecord(topic, "", "1"))
    }

    val end = System.nanoTime
    println(
      s"Published $messageCount messages in ${Duration.ofNanos(end - start).toMillis} ms"
    )

    // Закрываем и выходим
    producer.close()
    sys.exit(0)

  }
}
