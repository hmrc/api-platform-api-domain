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

package uk.gov.hmrc.apiplatform.modules.apis.domain.models

import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class LocatorSpec extends BaseJsonFormattersSpec {
  import play.api.libs.json.OFormat

  implicit val formatter: OFormat[Locator[String]] = Locator.buildLocatorFormatter[String]

  val sandbox: Locator[String]         = Locator.Sandbox("ABC")
  val otherSandbox: Locator[String]    = Locator.Sandbox("DEF")
  val production: Locator[String]      = Locator.Production("XYZ")
  val otherProduction: Locator[String] = Locator.Production("STU")
  val both: Locator.Both[String]       = Locator.Both("ABC", "XYZ")
  val otherBoth: Locator.Both[String]  = Locator.Both("DEF", "STU")

  "Locator" should {

    "lift to sandbox" in {
      import LocatorSyntax._

      "ABC".toSandbox shouldBe sandbox

      "ABC".toLocator(true) shouldBe sandbox
    }

    "lift to production" in {
      import LocatorSyntax._

      "XYZ".toProduction shouldBe production

      "XYZ".toLocator(false) shouldBe production
    }

    "add the other side to sandbox" in {
      sandbox.combine(production) shouldBe both
      sandbox.combine(both) shouldBe both
      production.combine(sandbox) shouldBe both
      production.combine(both) shouldBe both
    }

    "add to same side overides original locator" in {
      sandbox.combine(otherSandbox) shouldBe otherSandbox
      production.combine(otherProduction) shouldBe otherProduction
    }

    "filter" in {
      both.filterSandbox() shouldBe sandbox
      both.filterProduction() shouldBe production
    }

    "map works" in {
      import LocatorSyntax._
      val fn: (String) => Int = _.length
      sandbox.map(fn) shouldBe 3.toSandbox
      production.map(fn) shouldBe 3.toProduction
      both.map(fn) shouldBe Locator.Both(3, 3)
    }

    "both correctly combines" in {
      both.combine(otherSandbox) shouldBe Locator.Both("DEF", "XYZ")
      both.combine(otherProduction) shouldBe Locator.Both("ABC", "STU")
      both.combine(otherBoth) shouldBe otherBoth
    }

    "read sandbox from Json" in {
      testFromJson[Locator[String]]("""{ "location": "SANDBOX", "value": "ABC"}""")(sandbox)
    }

    "read production from Json" in {
      testFromJson[Locator[String]]("""{ "location": "PRODUCTION", "value": "XYZ"}""")(production)
    }

    "read both from Json" in {
      testFromJson[Locator[String]]("""{ "location": "BOTH", "sandboxValue": "ABC", "productionValue": "XYZ"}""")(both)
    }

    "write sandbox to Json" in {
      testToJson(sandbox)("location" -> "SANDBOX", "value" -> "ABC")
    }

    "write production to Json" in {
      testToJson(production)("location" -> "PRODUCTION", "value" -> "XYZ")
    }

    "write both to Json" in {
      testToJson[Locator[String]](both)("location" -> "BOTH", "sandboxValue" -> "ABC", "productionValue" -> "XYZ")
    }
  }
}
