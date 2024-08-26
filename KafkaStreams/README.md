# Примеры приложений на Kafka Streams

* **KafkaStreamWordCount** - Word Count.
* **DSL** - приложение на DSL, которое подсчитывает количество просмотров страниц пользователями по их регионам и выводит результат в формате Avro в тему "PageViewsByRegion".
* **Processor** - приложение на Processor API, которое подсчитывает количество просмотров страниц пользователями по их регионам и выводит результат в формате Avro в тему "PageViewsByRegion".

Схема темы:
```
{
	"name": "PageViewWithRegion",
	"type": "record",
	"fields": [
     		{"name": "region", "type": "string"}
     		{"name": "pages", "type": "number"},
	]
}
```
