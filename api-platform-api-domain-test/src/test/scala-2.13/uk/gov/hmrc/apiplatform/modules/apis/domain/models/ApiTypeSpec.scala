/*
 * Copyright 2026 HM Revenue & Customs
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

import play.api.libs.json._

import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class ApiTypeSpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks {

  val values =
    Table(
      ("Type", "text"),
      (ApiType.REST_API, "REST_API"),
      (ApiType.XML_API, "XML_API")
    )

  "ApiType" should {
    "read from Json" in {
      forAll(values) { (s, t) =>
        testFromJson[ApiType](s""""$t"""")(s)
      }
    }

    "read with error from Json" in {
      intercept[Exception] {
        testFromJson[ApiType](s"""123""")(ApiType.REST_API)
      }.getMessage() should include("Cannot parse API Type from '123'")
    }

    "write to Json" in {
      forAll(values) { (s, t) =>
        Json.toJson[ApiType](s) shouldBe JsString(t.toUpperCase())
      }
    }
  }
}
