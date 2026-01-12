package ru.example.kafka.scala

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.common.TopicPartition
import java.util

class MyConsumerRebalanceListener extends ConsumerRebalanceListener {

  override def onPartitionsRevoked(collection: util.Collection[TopicPartition]): Unit = {
    println("\nPartitions Revoked")
    collection.forEach { tp =>
      println(s"Topic: ${tp.topic()}, Partition: ${tp.partition()}")
    }
  }

  override def onPartitionsAssigned(collection: util.Collection[TopicPartition]): Unit = {
    println("\nPartitions Assigned")
    collection.forEach { tp =>
      println(s"Topic: ${tp.topic()}, Partition: ${tp.partition()}")
    }
  }
}
