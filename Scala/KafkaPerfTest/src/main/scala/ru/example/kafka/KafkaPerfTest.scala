package ru.example.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import java.time.Duration
import org.apache.kafka.clients.producer.ProducerConfig._
import org.apache.kafka.common.serialization._
import java.util.Properties

object KafkaPerfTest {
  def main(args: Array[String]): Unit = {
    if (args.length != 5) {
      println("Usage: KafkaPerfTest brokers topic messageCount messageSize")
      sys.exit(-1)
    }

    val brokers      = args(0)
    val topic        = args(1)
    val messageCount = args(2).toLong
    val messageSize  = args(3).toInt
    val batchSize    = args(4).toInt

    // Создаём Producer
    val props = new Properties()
    props.put(BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer])
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, classOf[ByteArraySerializer])
    props.put(BATCH_SIZE_CONFIG, batchSize)

    val producer = new KafkaProducer[Long, Array[Byte]](props)

    // Создаём сообщение, которое будем отправлять
    val value = Array.fill[Byte](messageSize)(0)

    // Отправляем в Kafka
    val start = System.nanoTime
    (0L until messageCount) foreach { _ =>
      producer.send(new ProducerRecord(topic, value))
    }
    val end = System.nanoTime

    // Выводим результат
    println(
      s"Published $messageCount messages with size $messageSize by batch $batchSize in ${Duration.ofNanos(end - start).toMillis} ms"
    )

    // Закрываем и выходим
    producer.close()
    sys.exit(0)
  }
}
