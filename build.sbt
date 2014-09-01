name := "rxtx-akka-io"

organization := "ch.inventsoft.akka"

scalaVersion := "2.11.2"

version := "1.0.4-SNAPSHOT"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

homepage := Some(url("https://github.com/msiegenthaler/rxtx-akka-io"))


resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.4"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"

libraryDependencies += "org.rxtx" % "rxtx" % "2.1.7"


libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.1" % "test"



publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false}

pomExtra := (
  <scm>
    <url>git@github.com:msiegenthaler/rxtx-akka-io</url>
    <connection>scm:git:git@github.com:msiegenthaler/rxtx-akka-io</connection>
  </scm>
  <developers>
    <developer>
      <id>msiegenthaler</id>
      <name>Mario Siegenthaler</name>
      <url>https://github.com/msiegenthaler</url>
    </developer>
  </developers>)