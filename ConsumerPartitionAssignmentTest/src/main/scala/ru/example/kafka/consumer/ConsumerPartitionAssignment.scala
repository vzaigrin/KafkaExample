package ru.example.kafka.consumer

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters.IterableHasAsJava

object ConsumerPartitionAssignment {
  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: ConsumerPartitionAssignment strategy")
      println("Strategy:")
      println("\tRangeAssignor")
      println("\tRoundRobinAssignor")
      println("\tStickyAssignor")
      println("\tCooperativeStickyAssignor")
      sys.exit(-1)
    }

    // Параметры
    val servers = "kafka1:9092,kafka2:9092,kafka3:9092"
    val topic1   = "t1"
    val topic2   = "t2"
    val group   = "g1"
    val pas = s"org.apache.kafka.clients.consumer.${args(0)}"

    // Создаём Consumer и подписываемся на темы
    val props = new Properties()
    props.put("bootstrap.servers", servers)
    props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("group.id", group)
    props.put("partition.assignment.strategy", pas)

    val consumer = new KafkaConsumer(props)
    consumer.subscribe(List(topic1, topic2).asJavaCollection, new MyConsumerRebalanceListener())

    // Читаем темы
    consumer.poll(Duration.ofSeconds(1))

    // Получаем информацию о темах и разделах и выводим её на экран
    println("\nAfter subscribe")
    val ass = consumer.assignment()
    ass.forEach { tp => println(s"Topic: ${tp.topic()}, Partition: ${tp.partition()}")
    }

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
