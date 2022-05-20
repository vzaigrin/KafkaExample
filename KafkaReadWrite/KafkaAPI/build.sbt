ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.4"

lazy val configVersion  = "1.4.2"
lazy val kafkaVersion   = "3.1.0"
lazy val csvVersion     = "1.9.0"
lazy val circeVersion   = "0.14.1"
lazy val logbackVersion = "1.2.11"

ThisBuild / libraryDependencies ++= Seq(
  "com.typesafe"       % "config"          % configVersion,
  "org.apache.kafka"   % "kafka-clients"   % kafkaVersion,
  "org.apache.commons" % "commons-csv"     % csvVersion,
  "io.circe"          %% "circe-core"      % circeVersion,
  "io.circe"          %% "circe-generic"   % circeVersion,
  "io.circe"          %% "circe-parser"    % circeVersion,
  "ch.qos.logback"     % "logback-classic" % logbackVersion
)

lazy val producer = project
  .settings(assembly / mainClass := Some("ru.example.kafka.producer.Producer"))
  .settings(assembly / assemblyJarName := "producer.jar")

lazy val consumer = project
  .settings(assembly / mainClass := Some("ru.example.kafka.consumer.Consumer"))
  .settings(assembly / assemblyJarName := "consumer.jar")
