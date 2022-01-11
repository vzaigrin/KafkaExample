package ru.example.ziokafka.consumer

import com.typesafe.config.ConfigFactory
import org.apache.kafka.common.TopicPartition
import zio.blocking.Blocking
import zio.clock.Clock
import zio.config.magnolia.descriptor
import zio.config.typesafe.TypesafeConfigSource
import zio.{ExitCode, Has, RIO, Task, URIO, ZIO, ZLayer}
import zio.console.{Console, putStrLn}
import zio.kafka.consumer.Consumer.{AutoOffsetStrategy, OffsetRetrieval}
import zio.kafka.consumer.{Consumer, ConsumerSettings, _}
import zio.kafka.serde.Serde
import zio.stream.ZStream
import zio.duration._

final case class AppConfig(
    brokers: String,
    topic: String,
    group: String,
    maxPartitions: Int,
    maxMsgForPartition: Int
)

object ZConsumer extends zio.App {
  import zio.config._

  val appConfig: Task[AppConfig] =
    for {
      rawConfig    <- ZIO.effect(ConfigFactory.load().getConfig("consumer"))
      configSource <- ZIO.fromEither(TypesafeConfigSource.fromTypesafeConfig(rawConfig))
      desc = descriptor[AppConfig] from configSource
      config <- ZIO.fromEither(read(desc))
    } yield config

  def endOffsets(appConfig: AppConfig): RIO[Has[Consumer], Map[TopicPartition, Long]] = {
    val tp =
      (0 until appConfig.maxPartitions).map(p => new TopicPartition(appConfig.topic, p)).toSet
    Consumer.endOffsets(tp, Duration.Infinity)
  }

  def consumer(
      appConfig: AppConfig,
      mtp: Map[TopicPartition, Long]
  ): ZStream[Console with Any with Has[Consumer], Throwable, Nothing] = {
    val partitionOffset: Map[Int, Long] = mtp.toList.flatMap(m => Map(m._1.partition -> m._2)).toMap

    Consumer
      .subscribeAnd(Subscription.topics(appConfig.topic))
      .plainStream(Serde.long, Serde.string)
      .filter { r =>
        r.offset.offset >= partitionOffset
          .getOrElse(
            r.partition,
            appConfig.maxMsgForPartition.toLong
          ) - appConfig.maxMsgForPartition
      }
      .take(appConfig.maxPartitions * appConfig.maxMsgForPartition)
      .tap(r => putStrLn(s"${r.partition}\t${r.offset.offset}\t${r.key}\t${r.value}"))
      .drain
  }

  def consumerLayer(appConfig: AppConfig): ZLayer[Clock with Blocking, Throwable, Has[Consumer]] = {
    ZLayer.fromManaged(
      Consumer.make(
        ConsumerSettings(List(appConfig.brokers))
          .withGroupId(appConfig.group)
          .withOffsetRetrieval(OffsetRetrieval.Auto(AutoOffsetStrategy.Earliest))
      )
    )
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    appConfig.flatMap { config =>
      endOffsets(config)
        .flatMap(mtp => consumer(config, mtp).runDrain)
        .provideCustomLayer(consumerLayer(config))
    }.exitCode
  }
}
