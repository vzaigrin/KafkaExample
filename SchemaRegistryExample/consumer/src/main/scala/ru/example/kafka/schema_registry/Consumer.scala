package ru.example.kafka.schema_registry

import com.typesafe.config.ConfigFactory
import io.confluent.kafka.serializers.{AbstractKafkaSchemaSerDeConfig, KafkaAvroDeserializer}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.{LongDeserializer, StringDeserializer}
import java.util.Properties
import java.time.Duration
import scala.util.{Failure, Success, Using}
import scala.jdk.CollectionConverters._

object Consumer {
  def main(args: Array[String]): Unit = {
    // Читаем конфигурационный файл
    val config = ConfigFactory.load()
    val topic  = config.getString("topic")

    // Создаём свойства для Consumer
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("bootstrap.servers"))
    props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, config.getString("schema.registry.url"))
    props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getString("group.id"))
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[LongDeserializer])
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer])

    // Создаём Consumer, подписываемся на тему и читаем записи
    Using.Manager { use =>
      val consumer = use(new KafkaConsumer[Long, GenericRecord](props))
      consumer.subscribe(List(topic).asJava)
      while (true) {
        val records: ConsumerRecords[Long, GenericRecord] = consumer.poll(Duration.ofSeconds(1))
        records.forEach { record =>
          val offset: Long         = record.offset()
          val key: Long            = record.key
          val value: GenericRecord = record.value
          println(s"offset = $offset\tkey = $key\tvalue = $value")
        }
      }
    } match {
      case Failure(ex) => println(ex.getLocalizedMessage)
      case Success(_)  =>
    }
  }
}
