package ru.example.structuredstreaming.consumer

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object SConsumer {
  def main(args: Array[String]): Unit = {
    // Читаем конфигурационный файл
    val config             = ConfigFactory.load()
    val bootstrapServers   = config.getString("bootstrap.servers")
    val topic              = config.getString("topic")
    val maxMsgForPartition = config.getInt("maxMsgForPartition")

    // Создаём SparkSession
    val spark = SparkSession.builder
      .appName("SConsumer")
      .getOrCreate()

    import spark.implicits._

    // Читаем из Kafka
    val books = spark.read
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option("subscribe", topic)
      .load()
      .selectExpr("CAST(partition AS INT)", "CAST(offset AS LONG)", "CAST(value AS STRING)")
      .as[Record]

    // Получаем максимальные смещения для всех разделов
    val maxOffsets = books
      .groupBy("partition")
      .agg(max("offset").as("offset"))
      .as[MaxOffsets]
      .collect
      .flatMap(mo => Map(mo.partition -> mo.offset))
      .toMap

    // Создаём UDF для проверки смещения в разделе
    val checkOffsetUDF = udf((partition: Int, offset: Long, max: Int) => offset > maxOffsets(partition) - max)

    // Выводим последние записи для каждого раздела
    books
      .filter(checkOffsetUDF($"partition", $"offset", lit(maxMsgForPartition)))
      .foreach { r => println(s"${r.partition}\t${r.offset}\t${r.value}") }
  }
}
