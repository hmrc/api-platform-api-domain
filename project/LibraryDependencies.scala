import sbt._

object LibraryDependencies {
  val scalatestVersion = "3.2.17"
  val commonDomainVersion = "0.11.0"

  def apply() = compileDependencies ++ testDependencies

  lazy val compileDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain"       % commonDomainVersion
  )

  lazy val testDependencies = Seq(
    "com.vladsch.flexmark"     % "flexmark-all"                     % "0.62.2",
    "org.mockito"             %% "mockito-scala-scalatest"          % "1.17.29",
    "org.scalactic"           %% "scalactic"                        % scalatestVersion,
    "org.scalatest"           %% "scalatest"                        % scalatestVersion,
    "uk.gov.hmrc"             %% "api-platform-test-common-domain"  % commonDomainVersion,
    ).map(_ % "test")
}
