lazy val commonSettings = Seq(
  organization := "ru.example",
  version := "1.0",
  scalaVersion := "2.12.12",
  libraryDependencies ++= Seq(
    "com.typesafe"     % "config"                    % "1.4.1",
    "org.apache.spark" % "spark-sql_2.12"            % "3.0.1" % "provided",
    "org.apache.spark" % "spark-streaming_2.12"      % "3.0.1" % "provided",
    "org.apache.spark" % "spark-sql-kafka-0-10_2.12" % "3.0.1",
    "io.circe"        %% "circe-core"                % "0.13.0",
    "io.circe"        %% "circe-generic"             % "0.13.0",
    "io.circe"        %% "circe-parser"              % "0.13.0"
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
