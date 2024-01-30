import scoverage.ScoverageKeys
import sbt._
import sbt.Keys._
import uk.gov.hmrc.DefaultBuildSettings.targetJvm
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import bloop.integrations.sbt.BloopDefaults

val appName = "api-platform-api-domain"
lazy val scala213 = "2.13.12"

ThisBuild / majorVersion     := 0
ThisBuild / isPublicArtefact := true
ThisBuild / scalaVersion     := scala213

ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val library = (project in file("."))
  .settings(
    publish / skip := true
  )
  .aggregate(
    apiPlatformApiDomain, apiPlatformTestApiDomain
  )

lazy val apiPlatformApiDomain = Project("api-platform-api-domain", file("api-platform-api-domain"))
  .settings(
    libraryDependencies ++= LibraryDependencies.commonDomain,
    ScoverageSettings(),
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-eT")
  )
  .disablePlugins(JUnitXmlReportPlugin)

lazy val apiPlatformTestApiDomain = Project("api-platform-test-api-domain", file("api-platform-test-api-domain"))
  .dependsOn(
    apiPlatformApiDomain
  )
  .settings(
    libraryDependencies ++= LibraryDependencies.root,
    ScoverageKeys.coverageEnabled := false,
  )
  .disablePlugins(JUnitXmlReportPlugin)

commands ++= Seq(
  Command.command("run-all-tests") { state => "test" :: state },

  Command.command("clean-and-test") { state => "clean" :: "compile" :: "run-all-tests" :: state },

  // Coverage does not need compile !
  Command.command("pre-commit") { state => "clean" :: "scalafmtAll" :: "scalafixAll" ::"coverage" :: "run-all-tests" :: "coverageReport" :: "coverageOff" :: state }
)

Global / bloopAggregateSourceDependencies := true
