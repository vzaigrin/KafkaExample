lazy val sparkVersion  = "3.1.1"
lazy val circeVersion  = "0.13.0"
lazy val configVersion = "1.4.1"

lazy val commonSettings = Seq(
  organization := "ru.example",
  version := "1.0",
  scalaVersion := "2.12.12",
  libraryDependencies ++= Seq(
    "com.typesafe"     % "config"                    % configVersion,
    "org.apache.spark" % "spark-sql_2.12"            % sparkVersion % "provided",
    "org.apache.spark" % "spark-streaming_2.12"      % sparkVersion % "provided",
    "org.apache.spark" % "spark-sql-kafka-0-10_2.12" % sparkVersion,
    "io.circe"        %% "circe-core"                % circeVersion,
    "io.circe"        %% "circe-generic"             % circeVersion,
    "io.circe"        %% "circe-parser"              % circeVersion
  ),
  assemblyMergeStrategy in assembly := {
    case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
    case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
    case "reference.conf"                                 => MergeStrategy.concat
    case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
    case _                                                => MergeStrategy.first
  }
)

lazy val producer = project
  .settings(commonSettings: _*)
  .settings(mainClass in assembly := Some("ru.example.structuredstreaming.producer.SProducer"))
  .settings(assemblyJarName in assembly := "producer.jar")

lazy val consumer = project
  .settings(commonSettings: _*)
  .settings(mainClass in assembly := Some("ru.example.structuredstreaming.consumer.SConsumer"))
  .settings(assemblyJarName in assembly := "consumer.jar")
