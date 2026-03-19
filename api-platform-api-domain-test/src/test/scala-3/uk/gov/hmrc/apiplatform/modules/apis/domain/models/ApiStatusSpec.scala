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

import scala.util.Random

import org.scalatest.prop.TableDrivenPropertyChecks

import play.api.libs.json.{JsString, Json}

import uk.gov.hmrc.apiplatform.modules.common.utils.*

class ApiStatusSpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks {

  "ApiStatus" should {
    val values =
      Table(
        ("Status", "text"),
        (ApiStatus.Alpha, "Alpha"),
        (ApiStatus.Beta, "Beta"),
        (ApiStatus.Stable, "Stable"),
        (ApiStatus.Deprecated, "Deprecated"),
        (ApiStatus.Retired, "Retired")
      )

    "convert lower case string to case object" in {
      forAll(values) { (s, t) =>
        ApiStatus.apply(t) shouldBe Some(s)
        ApiStatus.unsafeApply(t) shouldBe s
      }
    }

    "convert mixed case string to case object" in {
      forAll(values) { (s, t) =>
        ApiStatus.apply(t.toUpperCase()) shouldBe Some(s)
        ApiStatus.unsafeApply(t.toUpperCase()) shouldBe s
      }
    }

    "convert string value to None when undefined or empty" in {
      ApiStatus.apply("rubbish") shouldBe None
      ApiStatus.apply("") shouldBe None
    }

    "throw when string value is invalid" in {
      intercept[RuntimeException] {
        ApiStatus.unsafeApply("rubbish")
      }.getMessage() should include("API Status")
    }

    "read from Json" in {
      forAll(values) { (s, t) =>
        testFromJson[ApiStatus](s""""$t"""")(s)
        testFromJson[ApiStatus](s""""${t.toUpperCase()}"""")(s)
      }
    }

    "write to Json" in {
      forAll(values) { (s, t) =>
        Json.toJson[ApiStatus](s) shouldBe JsString(t.toUpperCase())
      }
    }

    "order correctly" in {
      val items = List.from(ApiStatus.values)

      Random.shuffle(items).sorted shouldBe items
    }

    "sort according to priority order" in {
      val statusesInPriorityOrder = List(ApiStatus.Stable, ApiStatus.Beta, ApiStatus.Alpha, ApiStatus.Deprecated, ApiStatus.Retired)

      Random.shuffle(statusesInPriorityOrder).sorted(using ApiStatus.orderingByPriority) shouldBe statusesInPriorityOrder
    }
  }
}
