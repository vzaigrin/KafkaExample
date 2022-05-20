package ru.example.ziokafka.producer

import com.typesafe.config.ConfigFactory
import org.apache.commons.csv.CSVFormat
import org.apache.kafka.clients.producer.ProducerRecord
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.config.magnolia.descriptor
import zio.config.typesafe._
import zio.console.Console
import zio.duration.durationInt
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde._
import zio.stream.ZStream
import zio.json._
import zio.logging.slf4j.Slf4jLogger
import zio.logging.{Logging, log}
import java.io.FileReader

final case class AppConfig(
    brokers: String,
    topic: String,
    filename: String
)

object ZProducer extends zio.App {
  import zio.config._

  val appConfig: Task[AppConfig] =
    for {
      rawConfig <- ZIO.effect(ConfigFactory.load().getConfig("producer"))
      configSource <- ZIO.fromEither(
        TypesafeConfigSource.fromTypesafeConfig(rawConfig)
      )
      desc = descriptor[AppConfig] from configSource
      config <- ZIO.fromEither(read(desc))
    } yield config

  def producer(
      appConfig: AppConfig
  ): ZStream[Any with Has[
    Producer
  ] with Logging with Clock, Throwable, Nothing] = {
    val in = new FileReader(appConfig.filename)
    val csvFormat =
      CSVFormat.RFC4180.builder().setHeader().setSkipHeaderRecord(true).build()
    val records = csvFormat.parse(in).iterator()

    ZStream
      .fromJavaIterator(records)
      .schedule(Schedule.fixed(1.seconds))
      .map { record =>
        new ProducerRecord(
          appConfig.topic,
          record.getRecordNumber,
          Book(record).toJson
        )
      }
      .mapM { producerRecord =>
        log.info(s"Producing $producerRecord") *>
          Producer.produce[Any, Long, String](
            producerRecord,
            keySerializer = Serde.long,
            valueSerializer = Serde.string
          )
      }
      .drain
  }

  def appLayer(
      appConfig: AppConfig
  ): ZLayer[Any with Blocking, Throwable, Logging with Has[Producer]] = {
    val producerLayer = ZLayer.fromManaged(
      Producer.make(settings = ProducerSettings(List(appConfig.brokers)))
    )
    val loggingLayer = Slf4jLogger.make { (_, message) => message }
    loggingLayer ++ producerLayer
  }

  override def run(args: List[String]): URIO[ZEnv with Console, ExitCode] = {
    appConfig.flatMap { config =>
      producer(config).runDrain
        .provideCustomLayer(appLayer(config))
    }.exitCode
  }
}
