ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.16"

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients"   % "3.9.1",
  "ch.qos.logback"   % "logback-classic" % "1.5.21"
)

lazy val root = (project in file("."))
  .settings(name := "ConsumerPartitionAssignment")
  .settings(assembly / mainClass := Some("ru.example.kafka.scala.ConsumerPartitionAssignment"))
  .settings(assembly / assemblyJarName := "ConsumerPartitionAssignment.jar")
  .settings(assembly / assemblyMergeStrategy := {
    case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
    case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
    case "module-info.class"                              => MergeStrategy.first
    case "version.conf"                                   => MergeStrategy.discard
    case "reference.conf"                                 => MergeStrategy.concat
    case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
    case _                                                => MergeStrategy.first
  })
