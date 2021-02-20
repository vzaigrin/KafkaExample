package ru.example.structuredstreaming.producer

import com.typesafe.config.ConfigFactory
import io.circe
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import org.apache.spark.sql._

object SProducer {
  def main(args: Array[String]): Unit = {
    // Читаем конфигурационный файл
    val config           = ConfigFactory.load()
    val input            = config.getString("input")
    val bootstrapServers = config.getString("bootstrap.servers")
    val topic            = config.getString("topic")

    // Encoder для Book
    implicit val bookEncoder: circe.Encoder[Book] = deriveEncoder[Book]

    // Создаём SparkSession
    val spark = SparkSession.builder
      .appName("SProducer")
      .getOrCreate()

    import spark.implicits._

    // Читаем файл с данными, преобразуем в JSON и выводим в Kafka
    spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(input)
      .withColumnRenamed("User Rating", "UserRating")
      .as[Book]
      .map(_.asJson.noSpaces)
      .toDF("value")
      .write
      .format("kafka")
      .option("topic", topic)
      .option("kafka.bootstrap.servers", bootstrapServers)
      .save()
  }
}
