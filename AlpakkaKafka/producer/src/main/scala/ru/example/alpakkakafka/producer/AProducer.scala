package ru.example.alpakkakafka.producer

import com.typesafe.config.ConfigFactory
import akka.Done
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Producer
import akka.kafka.ProducerSettings
import akka.stream.alpakka.csv.scaladsl.{CsvParsing, CsvToMap}
import akka.stream.scaladsl.FileIO
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import spray.json.{DefaultJsonProtocol, JsValue, JsonWriter}
import java.nio.file.Paths
import scala.concurrent.{ExecutionContextExecutor, Future}

object AProducer extends DefaultJsonProtocol {
  def main(args: Array[String]): Unit = {

    // Читаем конфигурационный файл
    val config           = ConfigFactory.load()
    val input            = config.getString("input")
    val bootstrapServers = config.getString("bootstrap.servers")
    val topic            = config.getString("topic")

    // Создаём систему акторов
    implicit val system: ActorSystem          = ActorSystem("producer")
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    // Настраиваем Producer
    val kafkaProducerSettings =
      ProducerSettings(system, new StringSerializer, new StringSerializer)
        .withBootstrapServers(bootstrapServers)

    // Читаем CSV файл, конвертируем в JSON, выводим
    val future: Future[Done] =
      FileIO
        .fromPath(Paths.get(input))
        .via(CsvParsing.lineScanner())
        .via(CsvToMap.toMapAsStrings())
        .map(toJson)
        .map(_.compactPrint)
        .zipWithIndex
        .map { elem => new ProducerRecord[String, String](topic, (elem._2 + 1).toString, elem._1) }
        .runWith(Producer.plainSink(kafkaProducerSettings))

    future.onComplete { _ =>
      println("Done!")
      system.terminate()
    }
  }

  def toJson(map: Map[String, String])(implicit jsWriter: JsonWriter[Map[String, String]]): JsValue =
    jsWriter.write(map)
}
