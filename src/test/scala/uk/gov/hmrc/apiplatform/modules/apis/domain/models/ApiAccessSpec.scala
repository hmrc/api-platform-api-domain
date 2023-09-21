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

import play.api.libs.json.{JsArray, Json}

import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApplicationId
import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class ApiAccessSpec extends BaseJsonFormattersSpec {

  val applicationId = ApplicationId.random

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
      testFromJson[ApiAccess](s"""{ "type": "PRIVATE", "whitelistedApplicationIds": ["${applicationId}"], "isTrial": true}""")(ApiAccess.Private(List(applicationId), true))
    }

    "read private access with fields from Json with planned field name" in {
      testFromJson[ApiAccess](s"""{ "type": "PRIVATE", "allowlistedApplicationIds": ["${applicationId}"], "isTrial": true}""")(ApiAccess.Private(List(applicationId), true))
    }

    "read private access is not tolerant without any fields" in {
      intercept[RuntimeException] {
        testFromJson[ApiAccess]("""{ "type": "PRIVATE"}""")(ApiAccess.Private(List(), false))
      }
    }

    "write to Json" in {
      Json.toJson[ApiAccess](ApiAccess.PUBLIC) shouldBe Json.obj("type" -> "PUBLIC")
      Json.toJson[ApiAccess](ApiAccess.Private(Nil, false)) shouldBe Json.obj(
        ("type"                      -> "PRIVATE"),
        ("whitelistedApplicationIds" -> JsArray()),
        ("isTrial"                   -> false)
      )
    }
  }
}
