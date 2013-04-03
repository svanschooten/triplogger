import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Triplogger"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.typesafe" %% "play-plugins-mailer" % "2.1.0",
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
