package ru.example.kafka.scala

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization._
import org.apache.kafka.clients.producer.ProducerConfig._
import java.util.Properties

object Producer {
  def main(args: Array[String]): Unit = {
    // Параметры
    val brokers = "localhost:9092"
    val topic   = "test"

    // Создаём Producer
    val props = new Properties()
    props.put(BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer])
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    val producer = new KafkaProducer[Int, String](props)

    // Генерируем записи
    try {
      (1 to 1000).foreach { i =>
        producer.send(new ProducerRecord(topic, i, s"Message $i"))
      }
    } catch {
      case e: Exception =>
        println(e.getLocalizedMessage)
        sys.exit(-1)
    } finally {
      producer.flush()
      producer.close()
    }

    sys.exit(0)
  }
}
