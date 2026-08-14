import sbt._

object LibraryDependencies {
  val scalatestVersion    = "3.2.19"
  val commonDomainVersion = "1.3.0"

  def domain(scalaVersion: String) =
    compileDependencies ++
    fixturesDependencies.map(_ % "test") ++ 
    commonTestDependencies(scalaVersion)

  def fixtures(scalaVersion: String) =
    compileDependencies ++
    fixturesDependencies.map(_ % "provided") ++ 
    commonTestDependencies(scalaVersion)

  def tests(scalaVersion: String) =
    compileDependencies ++
    fixturesDependencies.map(_ % "test") ++ 
    commonTestDependencies(scalaVersion)

  private val compileDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain"          % commonDomainVersion
  )

  private def fixturesDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain-fixtures" % commonDomainVersion
  )

  private def commonTestDependencies(scalaVersion: String) = (
    Seq(
      "com.vladsch.flexmark"     % "flexmark-all"                        % "0.64.8",
      "org.scalactic"           %% "scalactic"                           % scalatestVersion,
      "org.scalatest"           %% "scalatest"                           % scalatestVersion,
    )
  ).map(_ % "test")

}

