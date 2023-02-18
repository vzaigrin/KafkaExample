ThisBuild / organization := "ru.example"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.10"

lazy val kafkaVersion   = "3.4.0"
lazy val logbackVersion = "1.3.0"

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients"   % kafkaVersion,
  "ch.qos.logback"   % "logback-classic" % logbackVersion
)

lazy val root = (project in file(".")).settings(name := "KafkaPerfTest")

assembly / assemblyMergeStrategy := {
  case m if m.toLowerCase.endsWith("manifest.mf")       => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
  case m if m.toLowerCase.matches("version.conf")       => MergeStrategy.discard
  case "module-info.class"                              => MergeStrategy.first
  case "reference.conf"                                 => MergeStrategy.concat
  case x: String if x.contains("UnusedStubClass.class") => MergeStrategy.first
  case _                                                => MergeStrategy.first
}
