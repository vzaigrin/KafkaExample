# Примеры приложений с Avro

* **PageViews** - генератор сообщений в формате Avro для темы "PageViews".
* **UserProfileInit** - генератор инициализирующих сообщений в формате Avro для темы "UserProfiles".
* **UserProfile** - генератор сообщений с обновлениями в формате Avro для темы "UserProfiles".

Схемы тем:
```
{
	"name": "PageView",
	"type": "record",
 	"fields": [
		{"name": "user", "type": "string"},
		{"name": "page", "type": "string"},
		{"name": "industry", "type": "string"},
		{"name": "flags", "type": ["null", "string"], "default": null}
	]
}

{
	"name": "UserProfile",
	"type": "record",
	"fields": [
		{"name": "user", "type": "string"},
		{"name": "experience", "type": ["null", "string"], "default": null},
		{"name": "region", "type": "string"}
	]
}
```
