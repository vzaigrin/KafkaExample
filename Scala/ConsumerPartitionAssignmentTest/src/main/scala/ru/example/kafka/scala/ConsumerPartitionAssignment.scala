package ru.example.kafka.scala

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization._
import org.apache.kafka.clients.consumer.ConsumerConfig._
import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters.IterableHasAsJava

object ConsumerPartitionAssignment {
  def main(args: Array[String]): Unit = {

    if (args.length != 2) {
      println("Usage: ConsumerPartitionAssignment strategy brokers")
      println("Strategy:")
      println("\tRangeAssignor")
      println("\tRoundRobinAssignor")
      println("\tStickyAssignor")
      println("\tCooperativeStickyAssignor")
      sys.exit(-1)
    }

    // Параметры
    val brokers             = args(1)
    val topic1              = "t1"
    val topic2              = "t2"
    val group               = "g1"
    val partitionAssignment = s"org.apache.kafka.clients.consumer.${args(0)}"

    // Создаём Consumer и подписываемся на темы
    val props = new Properties()
    props.put(BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, classOf[IntegerDeserializer])
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    props.put(GROUP_ID_CONFIG, group)
    props.put(PARTITION_ASSIGNMENT_STRATEGY_CONFIG, partitionAssignment)

    val consumer = new KafkaConsumer(props)
    consumer.subscribe(List(topic1, topic2).asJavaCollection, new MyConsumerRebalanceListener())

    // Читаем темы
    consumer.poll(Duration.ofSeconds(1))

    // Получаем информацию о темах и разделах и выводим её на экран
    println("\nAfter subscribe")
    val ass = consumer.assignment()
    ass.forEach { tp => println(s"Topic: ${tp.topic()}, Partition: ${tp.partition()}") }

    // Читаем тему
    try {
      while (true) {
        consumer
          .poll(Duration.ofSeconds(1))
          .forEach { msg => println(s"${msg.partition}\t${msg.offset}\t${msg.key}\t${msg.value}") }
      }
    } catch {
      case e: Exception =>
        println(e.getLocalizedMessage)
        sys.exit(-1)
    } finally {
      consumer.close()
    }

    sys.exit(0)
  }
}
