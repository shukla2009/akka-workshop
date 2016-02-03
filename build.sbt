name := """akka-workshop"""

import com.typesafe.config.ConfigFactory

val conf = ConfigFactory.parseFile(new File("build.conf")).resolve()

version := conf.getString("build.version")

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.1",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.1" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.ning" % "async-http-client" % "1.9.31",
  "org.jsoup" % "jsoup" % "1.8.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.1")
