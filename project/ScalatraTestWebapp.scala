import _root_.de.johoop.jacoco4sbt.JacocoPlugin.jacoco

import sbt._
import sbt.Keys._
import scala._
import com.earldouglas.xsbtwebplugin._
import WebappPlugin._

object ScalatraTestWebapp extends Build {

  import Dependencies._

  lazy val pluginSettings = seq(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*) ++ seq(org.scalastyle.sbt.ScalastylePlugin.Settings: _*) ++ seq(jacoco.settings: _*) ++ seq(
    jacoco.reportFormats in jacoco.Config := Seq(de.johoop.jacoco4sbt.HTMLReport("utf-8"), de.johoop.jacoco4sbt.XMLReport("utf-8"))
  ) ++ seq(ScctPlugin.instrumentSettings: _*) ++ seq(
    unmanagedResourceDirectories in ScctPlugin.Scct <+= baseDirectory {
      _ / "src/main/webapp"
    }
  )


  lazy val container = Container("container")
  val scalatraVersion = "2.2.1"

  lazy val mainSettings = Defaults.defaultSettings ++ Seq(
    classpathTypes += "orbit",
    organization := "hu.finesolution.scalatratest",
    crossScalaVersions := Seq("2.10.2"),
    scalaVersion <<= crossScalaVersions {
      versions => versions.head
    },
    parallelExecution in Test := false,
    parallelExecution in jacoco.Config := false,
    parallelExecution in ScctPlugin.ScctTest := false,
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-Yinline-warnings", "-Xcheckinit", "-encoding", "utf8", "-feature"),
    scalacOptions ++= Seq("-language:higherKinds", "-language:postfixOps", "-language:implicitConversions", "-language:reflectiveCalls", "-language:existentials"),
    javacOptions ++= Seq("-target", "1.7", "-source", "1.7", "-Xlint:deprecation"),
    libraryDependencies ++= Seq(
      scalatraJson4s,
      json4s,
      scalatraTest % "test",
      servletApi % "provided"
    )
  ) ++ pluginSettings

  lazy val rootSettings = Defaults.defaultSettings ++ Seq(
    libraryDependencies ++= Seq(jetty, servletApi)
  ) ++ container.deploy(
    "/" -> webappRef
  )

  lazy val webappRef: ProjectReference = webapp

  lazy val webapp = Project(
    id = "webapp",
    base = file("webapp"),
    settings = mainSettings ++ Seq(
      libraryDependencies ++= Seq(
        scalatraScalate
      )
    )
  ) settings (webappSettings: _*) settings (net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)


  lazy val root = Project(id = "scalatratest-webapp", base = file(".")) settings (rootSettings: _*) settings (ScctPlugin.mergeReportSettings: _*) aggregate (webapp)


  object Dependencies {
    lazy val scalatraJson4s = "org.scalatra" %% "scalatra-json" % scalatraVersion
    lazy val scalatraScalate = "org.scalatra" %% "scalatra-scalate" % scalatraVersion
    lazy val scalatraTest = "org.scalatra" %% "scalatra-scalatest" % scalatraVersion
    lazy val json4s = "org.json4s" %% "json4s-jackson" % "3.2.5"
    lazy val jetty = "org.eclipse.jetty" % "jetty-annotations" % "8.1.10.v20130312" % "container"
    lazy val servletApi = "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" artifacts Artifact("javax.servlet", "jar", "jar")
    //    lazy val akka = "com.typesafe.akka" %% "akka-actor" % "2.2.0"
    //    lazy val casbah = "org.mongodb" %% "casbah" % "2.6.2"
    //    lazy val scopt = "com.github.scopt" %% "scopt" % "3.1.0"
    //    lazy val config = "com.typesafe" % "config" % "1.0.2"
    //    lazy val dispatch = "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"
    //    lazy val quartz = "org.quartz-scheduler" % "quartz" % "2.2.0"
    //    lazy val commonsCodec = "commons-codec" % "commons-codec" % "1.8"
    //    lazy val log4j = "org.slf4j" % "slf4j-log4j12" % "1.7.5"
  }

}