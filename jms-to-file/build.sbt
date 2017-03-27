name := "jms-to-file"

/*
  To uses this sbt project stand-alone,
  copy ../project/Dependencies.scala into ./project.
 */
import Dependencies._

libraryDependencies ++= Seq(
  alpakkaJms,
  akkaHttp,
  "javax.jms" % "jms-api" % "1.1-rev-1",
  "org.apache.activemq" % "activemq-all" % "5.14.4"
)