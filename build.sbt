name := """swiftcode"""

version := "1.7.2.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  evolutions,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.google.api-client" % "google-api-client" % "1.20.0",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.20.0",
  "com.google.apis" % "google-api-services-drive" % "v3-rev6-1.20.0",
  "org.logback-extensions" % "logback-ext-loggly" % "0.1.1",
  "com.typesafe.akka" %% "akka-remote" % "2.4.4"
)
