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

import play.api.libs.json.*

import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class CombinedApiSpec extends BaseJsonFormattersSpec {

  val aCombinedApi: CombinedApi =
    CombinedApi(
      "a display name",
      "a service name",
      Set(ApiCategory.Charities),
      ApiType.RestApi,
      ApiAccessType.Internal
    )

  val jsonText = """{"displayName":"a display name","serviceName":"a service name","categories":["CHARITIES"],"apiType":"REST_API","accessType":"INTERNAL"}"""

  "CombinedApi" should {
    "read from JSON" in {
      Json.parse(jsonText).as[CombinedApi] shouldBe aCombinedApi
    }

    "write to JSON" in {
      Json.toJson(aCombinedApi) shouldBe Json.parse(jsonText)
    }

    "read from old JSON" in {
      val oldJson = """{"displayName":"a display name","serviceName":"a service name","categories":["CHARITIES"],"apiType":"REST_API","accessType":"PRIVATE"}"""
      Json.parse(oldJson).as[CombinedApi] shouldBe aCombinedApi
    }

  }
}
