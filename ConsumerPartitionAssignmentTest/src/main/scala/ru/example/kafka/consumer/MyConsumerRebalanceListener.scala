package ru.example.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.common.TopicPartition
import java.util

class MyConsumerRebalanceListener extends ConsumerRebalanceListener {

  override def onPartitionsRevoked(collection: util.Collection[TopicPartition]): Unit = {
    println("\nonPartitionsRevoked")
  }

  override def onPartitionsAssigned(collection: util.Collection[TopicPartition]): Unit = {
    println("\nonPartitionsAssigned")
    collection.forEach { tp => println(s"Topic: ${tp.topic()}, Partition: ${tp.partition()}") }
  }
}