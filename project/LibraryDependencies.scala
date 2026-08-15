import sbt._

object LibraryDependencies {
  val commonDomainVersion = "1.4.0"

  def domain =
    compileDependencies

  def fixtures =
    compileDependencies ++
    fixturesDependencies.map(_ % "provided")

  def tests =
    compileDependencies ++
    fixturesDependencies.map(_ % "test") ++ 
    testDependencies

  private val compileDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain"          % commonDomainVersion % "provided"
  )

  private def fixturesDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain-fixtures" % commonDomainVersion
  )

  private def testDependencies = Seq.empty[ModuleID]
    .map(_ % "test")
}

