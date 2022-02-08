ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.4"

lazy val configVersion       = "1.4.1"
lazy val akkaVersion         = "2.6.11"
lazy val alpakkaVersion      = "2.0.2"
lazy val alpakkaKafkaVersion = "2.0.2"
lazy val jsonVersion         = "1.3.6"
lazy val logbackVersion      = "1.2.3"

ThisBuild / libraryDependencies ++= Seq(
  "com.typesafe"        % "config"                  % configVersion,
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % alpakkaVersion,
  "com.typesafe.akka"  %% "akka-stream-kafka"       % alpakkaKafkaVersion,
  "com.typesafe.akka"  %% "akka-stream"             % akkaVersion,
  "com.typesafe.akka"  %% "akka-actor"              % akkaVersion,
  "io.spray"            % "spray-json_2.13"         % jsonVersion,
  "com.typesafe.akka"  %% "akka-slf4j"              % akkaVersion,
  "ch.qos.logback"      % "logback-classic"         % logbackVersion
)

lazy val producer = project
  .settings(mainClass in assembly := Some("ru.example.alpakkakafka.producer.AProducer"))
  .settings(assemblyJarName in assembly := "producer.jar")

lazy val consumer = project
  .settings(mainClass in assembly := Some("ru.example.alpakkakafka.consumer.AConsumer"))
  .settings(assemblyJarName in assembly := "consumer.jar")
