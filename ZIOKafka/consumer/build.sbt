import sbtassembly.AssemblyPlugin.autoImport.assembly

ThisBuild / organization := "ru.example"
ThisBuild / scalaVersion := "2.13.7"
ThisBuild / version := "1.0"

lazy val zioVersion   = "1.0.13"
lazy val zioConfig    = "1.0.10"
lazy val zioKafka     = "0.17.3"
lazy val log4jVersion = "2.17.0"

ThisBuild / libraryDependencies ++= Seq(
  "dev.zio"                 %% "zio"                 % zioVersion,
  "dev.zio"                 %% "zio-streams"         % zioVersion,
  "dev.zio"                 %% "zio-kafka"           % zioKafka,
  "dev.zio"                 %% "zio-config-magnolia" % zioConfig,
  "dev.zio"                 %% "zio-config-typesafe" % zioConfig,
  "org.apache.logging.log4j" % "log4j-core"          % log4jVersion,
  "org.apache.logging.log4j" % "log4j-slf4j-impl"    % log4jVersion
)

assembly / assemblyMergeStrategy := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case "module-info.class"                        => MergeStrategy.first
  case _                                          => MergeStrategy.first
}
