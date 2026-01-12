(ns ru.vzaigrin.examples.kafka.base.clj.producer
  (:gen-class)
  (:import
   (java.util Properties)
   (org.apache.kafka.clients.producer KafkaProducer ProducerConfig ProducerRecord)
   (org.apache.kafka.common.serialization LongSerializer StringSerializer)))

(defn -main 
  "Base Kafka Producer"
  [& args]
  (if (not= (count args) 2)
    ((println "Usage: Producer topic brokers")
     (System/exit -1))
    (let [topic (first args)
          brokers (second args)
          props (doto (Properties.)
                  (.put ProducerConfig/BOOTSTRAP_SERVERS_CONFIG brokers)
                  (.put ProducerConfig/KEY_SERIALIZER_CLASS_CONFIG LongSerializer)
                  (.put ProducerConfig/VALUE_SERIALIZER_CLASS_CONFIG StringSerializer))]
      (with-open [producer (KafkaProducer. props)]
        (doseq [i (range 1 1001)] (.send producer (ProducerRecord. topic i (str " Message " i))))
        (.flush producer)))))
