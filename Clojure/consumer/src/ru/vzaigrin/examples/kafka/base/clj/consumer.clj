(ns ru.vzaigrin.examples.kafka.base.clj.consumer
  (:gen-class)
  (:import
   (java.time Duration)
   (java.util Properties)
   (org.apache.kafka.clients.consumer ConsumerConfig KafkaConsumer)
   (org.apache.kafka.common.serialization LongDeserializer StringDeserializer)))

(require '[clojure.string :as str])

(defn -main
  "Base Kafka Consumer"
  [& args]
  (if (not= (count args) 4)
    ((println "Usage: Consumer topics group brokers offset")
     (println "offset: 'earliest' or 'latest'")
     (System/exit -1))
    (let [topics (str/split (first args) #",")
          group (second args)
          brokers (nth args 2)
          offset (nth args 3)
          props (doto (Properties.)
                  (.put ConsumerConfig/BOOTSTRAP_SERVERS_CONFIG brokers)
                  (.put ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG LongDeserializer)
                  (.put ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG StringDeserializer)
                  (.put ConsumerConfig/GROUP_ID_CONFIG group)
                  (.put ConsumerConfig/AUTO_OFFSET_RESET_CONFIG offset)
                  (.put ConsumerConfig/ENABLE_AUTO_COMMIT_CONFIG "false"))]
      (with-open [consumer (KafkaConsumer. props)]
        (.subscribe consumer topics)
        (while true
          (doseq [msg (.poll consumer (Duration/ofSeconds 1))]
            (printf "%s\t%d\t%d\t%d\t%s\n"
                    (.topic msg)
                    (.partition msg)
                    (.offset msg)
                    (.key msg)
                    (.value msg))))))))
