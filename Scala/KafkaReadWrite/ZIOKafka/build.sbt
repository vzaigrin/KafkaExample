ThisBuild / organization := "ru.example"
ThisBuild / scalaVersion := "2.13.7"
ThisBuild / version := "1.0"

lazy val zioVersion = "1.0.14"
lazy val zioConfig = "1.0.10"
lazy val zioKafka = "0.17.5"
lazy val zioJSON = "0.1.5"
lazy val csvVersion = "1.9.0"
lazy val zioLogging = "0.5.14"
lazy val log4jVersion = "2.17.2"
lazy val disruptorVersion = "3.4.4"

ThisBuild / libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-kafka" % zioKafka,
  "dev.zio" %% "zio-config-magnolia" % zioConfig,
  "dev.zio" %% "zio-config-typesafe" % zioConfig,
  "dev.zio" %% "zio-json" % zioJSON,
  "org.apache.commons" % "commons-csv" % csvVersion,
  "dev.zio" %% "zio-logging" % zioLogging,
  "dev.zio" %% "zio-logging-slf4j" % zioLogging,
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
  "com.lmax" % "disruptor" % disruptorVersion
)

lazy val producer = project
  .settings(
    assembly / mainClass := Some("ru.example.ziokafka.producer.ZProducer")
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
    assembly / mainClass := Some("ru.example.ziokafka.consumer.ZConsumer")
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
