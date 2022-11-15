package ru.example.kafka.schema_registry

import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{LongSerializer, StringSerializer}
import io.confluent.kafka.serializers.{AbstractKafkaSchemaSerDeConfig, KafkaAvroSerializer}
import java.util.Properties
import scala.util.{Failure, Success, Using}
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord

object Producer {
  def main(args: Array[String]): Unit = {
    // Читаем конфигурационный файл
    val config = ConfigFactory.load()
    val topic  = config.getString("topic")

    // Создаём свойства для Producer
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("bootstrap.servers"))
    props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, config.getString("schema.registry.url"))
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.RETRIES_CONFIG, 0)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])

    // Создаём Producer и отправляем записи
    Using.Manager { use =>
      val producer = use(new KafkaProducer[Long, GenericRecord](props))

      // Отправляем 10 записей со схемой 1
      val parser1: Schema.Parser = new Schema.Parser()
      val paymentSchema1 =
        "{\"type\":\"record\", \"name\":\"Payment\", \"fields\":[{\"name\": \"id\", \"type\": \"long\"}, {\"name\": \"amount\", \"type\": \"double\"}]}"
      val schema1: Schema            = parser1.parse(paymentSchema1)
      val avroRecord1: GenericRecord = new GenericData.Record(schema1)
      avroRecord1.put("amount", 1000.00)
      send10(producer, topic, avroRecord1)

      // Отправляем 10 записей со схемой 2
      val parser2: Schema.Parser = new Schema.Parser()
      val paymentSchema2 =
        "{\"type\":\"record\", \"name\":\"Payment\", \"fields\":[{\"name\": \"id\", \"type\": \"long\"}, {\"name\": \"amount\", \"type\": \"double\"}, {\"name\": \"region\", \"type\": \"string\", \"default\": \"\"}]}"
      val schema2: Schema            = parser2.parse(paymentSchema2)
      val avroRecord2: GenericRecord = new GenericData.Record(schema2)
      avroRecord2.put("amount", 2000.00)
      avroRecord2.put("region", "Moscow")
      send10(producer, topic, avroRecord2)
    } match {
      case Failure(ex) => println(ex.getLocalizedMessage)
      case Success(_)  =>
    }
  }

  def send10(producer: KafkaProducer[Long, GenericRecord], topic: String, avroRecord: GenericRecord): Unit = {
    (0 until 10).foreach { i =>
//      val orderId = s"id$i"
      val orderId = i.toLong
      avroRecord.put("id", orderId)
      val record = new ProducerRecord(topic, orderId, avroRecord)
      producer.send(record)
      Thread.sleep(1000L)
    }
    producer.flush()
    println(s"Successfully produced 10 messages to a topic called $topic")
  }
}
