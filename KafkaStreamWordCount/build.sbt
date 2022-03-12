ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.12.15"

lazy val kafkaVersion = "3.1.0"
lazy val logbackVersion = "1.2.11"

lazy val root = (project in file("."))
  .settings(
    name := "KafkaStreamWordCount",
    libraryDependencies ++= Seq(
      "com.typesafe"      % "config"              % "1.4.2",
      "org.apache.kafka"  % "kafka-streams"       % kafkaVersion,
      "org.apache.kafka"  % "kafka-clients"       % kafkaVersion,
      "org.apache.kafka" %% "kafka-streams-scala" % kafkaVersion,
      "ch.qos.logback"    % "logback-classic"     % logbackVersion
    ),
    assembly / assemblyMergeStrategy := {
      case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
      case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
      case "reference.conf"                                 => MergeStrategy.concat
      case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
      case _                                                => MergeStrategy.first
    }
  )
