import sbt._

object LibraryDependencies {
  val scalatestVersion    = "3.2.19"
  val commonDomainVersion = "1.4.0"

  def domain =
    compileDependencies.map(_ % "provided")

  def fixtures =
    compileDependencies.map(_ % "provided") ++
    fixturesDependencies.map(_ % "provided")

  def tests =
    compileDependencies ++
    fixturesDependencies ++ 
    testDependencies.map(_ % "test")

  private val compileDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain"          % commonDomainVersion
  )

  private def fixturesDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain-fixtures" % commonDomainVersion
  )

  private def testDependencies = Seq.empty[ModuleID]
}

