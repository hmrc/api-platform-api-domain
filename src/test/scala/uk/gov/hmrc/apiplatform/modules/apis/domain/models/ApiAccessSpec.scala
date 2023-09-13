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

import play.api.libs.json.Json

import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec
import play.api.libs.json.JsArray

class ApiAccessSpec extends BaseJsonFormattersSpec {

  "ApiAccess" should {
    
    "display text correctly" in {
      ApiAccess.PUBLIC.displayText shouldBe "Public"
      ApiAccess.Private(Nil, true).displayText shouldBe "Private"
      ApiAccess.Private(Nil, false).displayText shouldBe "Private"
    }

    "provide access type" in {
      ApiAccess.PUBLIC.accessType shouldBe ApiAccessType.PUBLIC
      ApiAccess.Private(Nil, false).accessType shouldBe ApiAccessType.PRIVATE
    }

    "read public access from Json" in {
      testFromJson[ApiAccess]("""{ "type": "PUBLIC"}""")(ApiAccess.PUBLIC)
    }

    "read private access with fields from Json" in {
      testFromJson[ApiAccess]("""{ "type": "PRIVATE", "whitelistedApplicationIds": ["123"], "isTrial": true}""")(ApiAccess.Private(List("123"), true))
    }

    "read private access is not tolerant without any fields" in {
      intercept[RuntimeException] {
        testFromJson[ApiAccess]("""{ "type": "PRIVATE"}""")(ApiAccess.Private(List(), false))
      }
    }

    "write to Json" in {
      Json.toJson[ApiAccess](ApiAccess.PUBLIC) shouldBe Json.obj("type" -> "PUBLIC")
      Json.toJson[ApiAccess](ApiAccess.Private(Nil, false)) shouldBe Json.obj(
        ("type" -> "PRIVATE"),
        ("whitelistedApplicationIds" -> JsArray()),
        ("isTrial" -> false)
      )
    }
  }
}
