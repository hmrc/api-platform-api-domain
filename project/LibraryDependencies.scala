import sbt._

object LibraryDependencies {
  val scalatestVersion = "3.2.17"
  val commonDomainVersion = "0.16.0"

  lazy val commonDomain = compileDependencies ++ testDependencies.map(_ % "test")

  lazy val root = compileDependencies ++ testDependencies

  private lazy val compileDependencies = Seq(
    "uk.gov.hmrc"             %% "api-platform-common-domain"       % commonDomainVersion
  )

  private lazy val testDependencies = Seq(
    "com.vladsch.flexmark"     % "flexmark-all"                     % "0.62.2",
    "org.mockito"             %% "mockito-scala-scalatest"          % "1.17.29",
    "org.scalactic"           %% "scalactic"                        % scalatestVersion,
    "org.scalatest"           %% "scalatest"                        % scalatestVersion,
    "uk.gov.hmrc"             %% "api-platform-test-common-domain"  % commonDomainVersion,
    )
}
