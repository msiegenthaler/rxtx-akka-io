name := "rxtx-akka-io"

scalaVersion := "2.10.3"

version := "1.0.0"


resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"


libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.2.3"

libraryDependencies += "org.rxtx" % "rxtx" % "2.1.7"


libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"
