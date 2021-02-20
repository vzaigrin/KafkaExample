package ru.example.structuredstreaming.consumer

case class MaxOffsets(
    partition: Int,
    offset: Long
)
