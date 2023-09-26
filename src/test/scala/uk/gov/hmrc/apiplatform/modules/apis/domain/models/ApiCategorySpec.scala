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

import org.scalatest.prop.TableDrivenPropertyChecks

import play.api.libs.json.{JsString, Json}

import uk.gov.hmrc.apiplatform.modules.common.utils._

class ApiCategorySpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks {

  "ApiCategorySpec" should {
    val values =
      Table(
        ("Category", "text", "displayText"),
        (ApiCategory.AGENTS, "agents", "Agents"),
        (ApiCategory.BUSINESS_RATES, "business_rates", "Business Rates"),
        (ApiCategory.INCOME_TAX_MTD, "income_tax_mtd", "Income Tax (Making Tax Digital)")
      )

    "values are in a fixed order" in {
      ApiCategory.values.head shouldBe ApiCategory.EXAMPLE
      ApiCategory.values.last shouldBe ApiCategory.OTHER
      val others: List[ApiCategory] = ApiCategory.values.drop(1).dropRight(1).toList
      others.sortBy(_.toString) shouldBe others
    }

    "convert to string correctly" in {
      forAll(values) { (s, t, _) =>
        s.toString() shouldBe t.toUpperCase()
      }
    }

    "convert lower case string to case object" in {
      forAll(values) { (s, t, _) =>
        ApiCategory.apply(t) shouldBe Some(s)
        ApiCategory.unsafeApply(t) shouldBe s
      }
    }

    "convert mixed case string to case object" in {
      forAll(values) { (s, t, _) =>
        ApiCategory.apply(t.toUpperCase()) shouldBe Some(s)
        ApiCategory.unsafeApply(t.toUpperCase()) shouldBe s
      }
    }

    "convert string value to None when undefined or empty" in {
      ApiCategory.apply("rubbish") shouldBe None
      ApiCategory.apply("") shouldBe None
    }

    "throw when string value is invalid" in {
      intercept[RuntimeException] {
        ApiCategory.unsafeApply("rubbish")
      }.getMessage() should include("API Category")
    }

    "return display text for a given category" in {
      forAll(values) { (s, _, expectedDisplayText) =>
        s.displayText shouldBe expectedDisplayText
      }
    }

    "read from Json" in {
      forAll(values) { (s, t, _) =>
        testFromJson[ApiCategory](s""""$t"""")(s)
      }
    }

    "write to Json" in {
      forAll(values) { (s, t, _) =>
        Json.toJson[ApiCategory](s) shouldBe JsString(t.toUpperCase())
      }
    }
  }
}
