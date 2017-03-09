import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import de.heikoseeberger.sbtheader._
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport._

object Common extends AutoPlugin {

  val FileHeader = (HeaderPattern.cStyleBlockComment,
       """
         |""".stripMargin)

  override def trigger = allRequirements

  override def requires = JvmPlugin && HeaderPlugin

  override lazy val projectSettings = Seq(
    organization := "ScalaSthlm",
    organizationName := "Scala Stockholm",
    homepage := Some(url("https://github.com/ScalaSthlm/alpakka-integration-patterns")),
    scmInfo := Some(ScmInfo(url("https://github.com/ScalaSthlm/alpakka-integration-patterns"), "git@github.com:ScalaSthlm/alpakka-integration-patterns.git")),
    developers += Developer("contributors", "Contributors", "@ScalaSthlm", url("https://github.com/ScalaSthlm/alpakka-integration-patterns/graphs/contributors")),

    licenses := Seq(("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))),

    scalaVersion := crossScalaVersions.value.head,
    crossScalaVersions := Dependencies.ScalaVersions,
    crossVersion := CrossVersion.binary,

    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
      //"-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Xfuture"
    ),

    javacOptions ++= Seq(
      "-Xlint:unchecked"
    ),

    autoAPIMappings := true,
    apiURL := None,

    // show full stack traces and test case durations
    testOptions in Test += Tests.Argument("-oDF"),

    // -v Log "test run started" / "test started" / "test run finished" events on log level "info" instead of "debug".
    // -a Show stack traces and exception class name for AssertionErrors.
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a"),

    headers := headers.value ++ Map(
      "scala" -> FileHeader,
      "java" -> FileHeader
    ),

    ivyScala := ivyScala.value.map(_.copy(overrideScalaVersion = sbtPlugin.value)) // TODO Remove once this workaround no longer needed (https://github.com/sbt/sbt/issues/2786)!
  )
}
