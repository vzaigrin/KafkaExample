# Kafka Schema Registry Example

Простое приложение для демонстрации работы с Kafka Schema Registry

## Запуск

* Запускаем сервисы: *docker-compose up -d*
* Создаём топик: *docker exec schemaregistryexample-broker-1 /bin/kafka-topics --create --topic test --bootstrap-server localhost:29092*
* Запускаем producer: *java -jar producer/target/scala-2.13/producer.jar*
* Проверяем список схем топика: *curl --silent -X GET http://localhost:8081/subjects/test-value/versions | jq*
* Проверяем первую схему: *curl --silent -X GET http://localhost:8081/subjects/test-value/versions/1 | jq* 
* Проверяем вторую схему: *curl --silent -X GET http://localhost:8081/subjects/test-value/versions/2 | jq* 
* Запускаем consumer: *java -jar consumer/target/scala-2.13/consumer.jar*