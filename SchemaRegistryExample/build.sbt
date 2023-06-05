ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.8"

ThisBuild / resolvers += "Confluent Repository" at "https://packages.confluent.io/maven/"

ThisBuild / libraryDependencies ++= Seq(
  "com.typesafe"     % "config"                % "1.4.2",
  "org.apache.kafka" % "kafka-clients"         % "3.4.0",
  "io.confluent"     % "kafka-avro-serializer" % "7.4.0",
  "ch.qos.logback"   % "logback-classic"       % "1.4.6"
)

lazy val producer = project
  .settings(
    assembly / mainClass := Some("ru.example.kafka.schema_registry.Producer")
  )
  .settings(assembly / assemblyJarName := "producer.jar")
  .settings(assembly / assemblyMergeStrategy := {
    case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
    case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
    case "module-info.class"                              => MergeStrategy.first
    case "version.conf"                                   => MergeStrategy.discard
    case "reference.conf"                                 => MergeStrategy.concat
    case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
    case _                                                => MergeStrategy.first
  })

lazy val consumer = project
  .settings(
    assembly / mainClass := Some("ru.example.kafka.schema_registry.Consumer")
  )
  .settings(assembly / assemblyJarName := "consumer.jar")
  .settings(assembly / assemblyMergeStrategy := {
    case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
    case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
    case "module-info.class"                              => MergeStrategy.first
    case "version.conf"                                   => MergeStrategy.discard
    case "reference.conf"                                 => MergeStrategy.concat
    case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
    case _                                                => MergeStrategy.first
  })

assembly / assemblyMergeStrategy := {
  case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
  case m if m.toLowerCase.matches("version.conf")       => MergeStrategy.discard
  case "module-info.class"                              => MergeStrategy.first
  case "reference.conf"                                 => MergeStrategy.concat
  case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
  case _                                                => MergeStrategy.first
}
