ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.4"

lazy val configVersion       = "1.4.2"
lazy val akkaVersion         = "2.6.19"
lazy val alpakkaVersion      = "3.0.4"
lazy val alpakkaKafkaVersion = "3.0.0"
lazy val jsonVersion         = "1.3.6"
lazy val logbackVersion      = "1.2.11"

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
  .settings(assemblyMergeStrategy in assembly := {
    case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
    case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
    case "version.conf"                                   => MergeStrategy.discard
    case "reference.conf"                                 => MergeStrategy.concat
    case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
    case _                                                => MergeStrategy.first
  })

lazy val consumer = project
  .settings(mainClass in assembly := Some("ru.example.alpakkakafka.consumer.AConsumer"))
  .settings(assemblyJarName in assembly := "consumer.jar")
  .settings(assemblyMergeStrategy in assembly := {
    case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
    case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
    case "version.conf"                                   => MergeStrategy.discard
    case "reference.conf"                                 => MergeStrategy.concat
    case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
    case _                                                => MergeStrategy.first
  })

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
  case m if m.toLowerCase.matches("version.conf")       => MergeStrategy.discard
  case "reference.conf"                                 => MergeStrategy.concat
  case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
  case _                                                => MergeStrategy.first
}
