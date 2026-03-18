import sbt._

object LibraryDependencies {
  val scalatestVersion    = "3.2.19"
  val commonDomainVersion = "1.0.0"

  def domain(scalaVersion: String) =
    compileDependencies ++
    fixturesDependencies.map(_ % "test") ++ 
    commonTestDependencies(scalaVersion)

  def fixtures(scalaVersion: String) =
    compileDependencies ++
    fixturesDependencies ++ 
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
      "com.vladsch.flexmark"     % "flexmark-all"                        % "0.62.2",
      "org.scalactic"           %% "scalactic"                           % scalatestVersion,
      "org.scalatest"           %% "scalatest"                           % scalatestVersion,
    ) ++ (
      CrossVersion.partialVersion(scalaVersion) match {
        case Some((2,_)) => Seq("org.mockito" %% "mockito-scala-scalatest" % "2.0.0")
        case _           => Seq("org.scalatestplus" %% "mockito-5-18"      % "3.2.19.0")
      }
    )
  ).map(_ % "test")

}
