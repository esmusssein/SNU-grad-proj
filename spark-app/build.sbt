import _root_.sbtassembly.AssemblyPlugin.autoImport._
import _root_.sbtassembly.PathList

lazy val root = (project in file(".")).
  settings(
    name := "spark-app",
    version := "0.1",
    scalaVersion := "2.10.6",
    mainClass in Compile := Some("App")
  )

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka" % "1.6.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6"
libraryDependencies += "joda-time" % "joda-time" % "2.9.3"
libraryDependencies += "log4j" % "log4j" % "1.2.17"
libraryDependencies += "io.argonaut" %% "argonaut" % "6.1"

// META-INF discarding
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.concat
  case x => MergeStrategy.first
}
}

