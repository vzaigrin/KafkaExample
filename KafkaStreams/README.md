# Примеры приложений на Kafka Streams

* **KafkaStreamWordCount** - Word Count.
* **DSL** - приложение на DSL, которое подсчитывает количество просмотров страниц пользователями по их регионам и выводит результат в формате Avro в тему "PageViewWithRegion".
* **Processor** - приложение на Processor API, которое подсчитывает количество просмотров страниц пользователями по их регионам и выводит результат в формате Avro в тему "PageViewWithRegion".

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
