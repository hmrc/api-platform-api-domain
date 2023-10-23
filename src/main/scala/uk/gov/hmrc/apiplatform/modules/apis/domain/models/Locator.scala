/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// $COVERAGE-OFF$
package uk.gov.hmrc.apiplatform.modules.apis.domain.models

import play.api.libs.json._
import uk.gov.hmrc.play.json.Union

sealed trait Locator[T] {
  def sandbox: Option[T]
  def production: Option[T]

  def combine(other: Locator[T]): Locator[T]

  def map[A](fn: T => A): Locator[A]
}

object Locator {

  case class Sandbox[T](value: T) extends Locator[T] {

    val sandbox    = Some(value)
    val production = None

    override def combine(other: Locator[T]): Locator[T] = other match {
      case sandbox: Sandbox[T]       => sandbox
      case production: Production[T] => Both[T](value, production.value)
      case both: Both[T]             => both
    }

    def map[A](fn: T => A): Locator[A] = Sandbox(fn(value))
  }

  case class Production[T](value: T) extends Locator[T] {
    val sandbox    = None
    val production = Some(value)

    override def combine(other: Locator[T]): Locator[T] = other match {
      case sandbox: Sandbox[T]       => Both[T](sandbox.value, value)
      case production: Production[T] => other
      case both: Both[T]             => both
    }

    def map[A](fn: T => A): Locator[A] = Production(fn(value))
  }

  case class Both[T](sandboxValue: T, productionValue: T) extends Locator[T] {
    val sandbox    = Some(sandboxValue)
    val production = Some(productionValue)

    override def combine(other: Locator[T]): Locator[T] = other match {
      case sandbox: Sandbox[T]       => Both[T](sandbox.value, productionValue)
      case production: Production[T] => Both[T](sandboxValue, production.value)
      case both: Both[T]             => both
    }

    def filterSandbox(): Locator[T]    = Sandbox(sandboxValue)
    def filterProduction(): Locator[T] = Production(productionValue)

    def map[A](fn: T => A): Locator[A] = Both(fn(sandboxValue), fn(productionValue))
  }

  def buildLocatorFormatter[T](implicit fmt: Format[T]) = new OFormat[Locator[T]] {
    private implicit val formatSandbox: OFormat[Sandbox[T]]       = Json.format[Sandbox[T]]
    private implicit val formatProduction: OFormat[Production[T]] = Json.format[Production[T]]
    private implicit val formatBoth: OFormat[Both[T]]             = Json.format[Both[T]]

    private val formatter = Union.from[Locator[T]]("location")
      .and[Sandbox[T]]("SANDBOX")
      .and[Production[T]]("PRODUCTION")
      .and[Both[T]]("BOTH")
      .format

    override def writes(o: Locator[T]): JsObject = formatter.writes(o)

    override def reads(json: JsValue): JsResult[Locator[T]] = formatter.reads(json)
  }
}

object LocatorSyntax {

  implicit class LocatorSyntax[T](t: T) {
    def toSandbox: Locator[T]    = Locator.Sandbox[T](t)
    def toProduction: Locator[T] = Locator.Production[T](t)

    def toLocator(sandbox: Boolean) = if (sandbox) toSandbox else toProduction
  }

}
// $COVERAGE-ON$
