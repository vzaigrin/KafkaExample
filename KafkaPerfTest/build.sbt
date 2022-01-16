ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.4"

lazy val kafkaVersion   = "3.0.0"
lazy val logbackVersion = "1.2.10"

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients"   % kafkaVersion,
  "ch.qos.logback"   % "logback-classic" % logbackVersion
)

lazy val root = (project in file(".")).settings(name := "KafkaPerfTest")
