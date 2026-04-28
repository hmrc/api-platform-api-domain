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

import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class ApiAccessTypeSpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks {

  "ApiAccessType" should {
    val values =
      Table(
        ("Type", "text"),
        (ApiAccessType.Public, "public"),
        (ApiAccessType.Internal, "internal"),
        (ApiAccessType.Controlled, "controlled")
      )

    "convert lower case string to case object" in {
      forAll(values) { (s, t) =>
        ApiAccessType.apply(t) shouldBe Some(s)
        ApiAccessType.unsafeApply(t) shouldBe s
      }
    }

    "convert mixed case string to case object" in {
      forAll(values) { (s, t) =>
        ApiAccessType.apply(t.toUpperCase()) shouldBe Some(s)
        ApiAccessType.unsafeApply(t.toUpperCase()) shouldBe s
      }
    }

    "convert string value to None when undefined or empty" in {
      ApiAccessType.apply("rubbish") shouldBe None
      ApiAccessType.apply("") shouldBe None
    }

    "throw when string value is invalid" in {
      intercept[RuntimeException] {
        ApiAccessType.unsafeApply("rubbish")
      }.getMessage() should include("API Access Type")
    }

    "read from Json" in {
      forAll(values) { (s, t) =>
        testFromJson[ApiAccessType](s""""$t"""")(s)
      }
    }

    "read with error from Json" in {
      intercept[Exception] {
        testFromJson[ApiAccessType](s"""123""")(ApiAccessType.Public)
      }.getMessage() should include("Cannot parse API Access Type from '123'")
    }

    "read public access from Json" in {
      testFromJson[ApiAccessType]("""{ "type": "PUBLIC"}""")(ApiAccessType.Public)
    }

    "read controlled access from Json" in {
      testFromJson[ApiAccessType]("""{ "type": "CONTROLLED"}""")(ApiAccessType.Controlled)
    }

    "read internal access from Json" in {
      testFromJson[ApiAccessType]("""{ "type": "INTERNAL"}""")(ApiAccessType.Internal)
    }

    "read private access from Json" in {
      testFromJson[ApiAccessType]("""{ "type": "PRIVATE", "isTrial": false }""")(ApiAccessType.Internal)
    }

    "read private access from Json for trial" in {
      testFromJson[ApiAccessType]("""{ "type": "PRIVATE", "isTrial": true }""")(ApiAccessType.Controlled)
    }

    "read private access from Json with isTrial: null" in {
      testFromJson[ApiAccessType]("""{ "type": "PRIVATE", "isTrial": null }""")(ApiAccessType.Internal)
    }

    "read private access from Json without isTrial" in {
      testFromJson[ApiAccessType]("""{ "type": "PRIVATE" }""")(ApiAccessType.Internal)
    }

    "write to Json" in {
      forAll(values) { (s, t) =>
        Json.toJson[ApiAccessType](s) shouldBe JsString(t.toUpperCase())
      }
    }
  }
}
