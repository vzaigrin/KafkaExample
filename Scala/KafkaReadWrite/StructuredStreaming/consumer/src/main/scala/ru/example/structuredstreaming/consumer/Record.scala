package ru.example.structuredstreaming.consumer

case class Record(
    partition: Int,
    offset: Long,
    value: String
)
