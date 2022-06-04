# Kafka Schema Registry Example

Простое приложение для демонстрации работы с Kafka Schema Registry

## Запуск

* Запускаем сервисы: *docker-compose up -d*
* Создаём топик: *kafka-topics.sh --create --topic test --bootstrap-server localhost:29092*
* Запускаем producer: *java -jar producer.jar*
* Запускаем consumer: *java -jar consumer*
* Проверяем список схем топика: *curl --silent -X GET http://localhost:8081/subjects/test-value/versions | jq .
* Проверяем первую схему: *curl --silent -X GET http://localhost:8081/subjects/test-value/versions/1 | jq .* 
* Проверяем вторую схему: *curl --silent -X GET http://localhost:8081/subjects/test-value/versions/2 | jq .* 
