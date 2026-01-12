package ru.example.alpakkakafka.consumer

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.{Consumer, MetadataClient}
import akka.kafka.{ConsumerSettings, Subscriptions}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.concurrent.duration.DurationInt

object AConsumer {
  def main(args: Array[String]): Unit = {

    // Читаем конфигурационный файл
    val config             = ConfigFactory.load()
    val bootstrapServers   = config.getString("bootstrap.servers")
    val topic              = config.getString("topic")
    val groupId            = config.getString("group.id")
    val maxMsgForPartition = config.getInt("maxMsgForPartition")
    val maxPartitions      = config.getInt("maxPartitions")

    // Создаём систему акторов
    implicit val system: ActorSystem          = ActorSystem("consumer")
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    // Настраиваем Consumer
    val consumerSettings =
      ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
        .withBootstrapServers(bootstrapServers)
        .withGroupId(groupId)
        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    // Получаем смещения
    val metadataClient = MetadataClient.create(consumerSettings, 1.second)
    val partitionOffset =
      (0 until maxPartitions).map { p =>
        val tp     = new TopicPartition(topic, p)
        val offset = metadataClient.getEndOffsetForPartition(tp)
        tp.partition -> Await.result(offset, 5.second)
      }.toMap
    metadataClient.close()

    // Читаем из Kafka, оставляем последние 5 сообщений на секцию
    val future: Future[Done] =
      Consumer
        .plainPartitionedSource(consumerSettings, Subscriptions.topics(topic))
        .flatMapMerge(maxPartitions, _._2)
        .filter(cr => cr.offset >= partitionOffset(cr.partition) - maxMsgForPartition)
        .take(maxPartitions * maxMsgForPartition)
        .runForeach { cr => println(s"${cr.partition}\t${cr.offset}\t${cr.key}\t${cr.value}") }

    future.onComplete { _ =>
      println("Done!")
      system.terminate()
    }
  }
}
