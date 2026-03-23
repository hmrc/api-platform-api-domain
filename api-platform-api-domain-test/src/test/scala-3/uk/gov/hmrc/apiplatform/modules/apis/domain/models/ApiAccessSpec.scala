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

import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApplicationId
import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class ApiAccessSpec extends BaseJsonFormattersSpec {

  val applicationId = ApplicationId.random

  "ApiAccess" should {

    "display text correctly" in {
      ApiAccess.Public.displayText shouldBe "Public"
      ApiAccess.Internal.displayText shouldBe "Internal"
      ApiAccess.Controlled.displayText shouldBe "Controlled"
      ApiAccess.Private(true).displayText shouldBe "Private"
      ApiAccess.Private(false).displayText shouldBe "Private"
    }

    "provide access type" in {
      ApiAccess.Public.accessType shouldBe ApiAccessType.Public
      ApiAccess.Internal.accessType shouldBe ApiAccessType.Internal
      ApiAccess.Controlled.accessType shouldBe ApiAccessType.Controlled
      ApiAccess.Private(true).accessType shouldBe ApiAccessType.Private
      ApiAccess.Private(false).accessType shouldBe ApiAccessType.Private
    }

    "read public access from Json" in {
      testFromJson[ApiAccess]("""{ "type": "PUBLIC"}""")(ApiAccess.Public)
    }

    "read controlled access from Json" in {
      testFromJson[ApiAccess]("""{ "type": "CONTROLLED"}""")(ApiAccess.Controlled)
    }

    "read internal access from Json" in {
      testFromJson[ApiAccess]("""{ "type": "INTERNAL"}""")(ApiAccess.Internal)
    }

    "read private access from Json" in {
      testFromJson[ApiAccess]("""{ "type": "PRIVATE", "isTrial": false }""")(ApiAccess.Private(false))
    }

    "read private access from Json for trial" in {
      testFromJson[ApiAccess]("""{ "type": "PRIVATE", "isTrial": true }""")(ApiAccess.Private(true))
    }

    "read private access from json applies default" in {
      testFromJson[ApiAccess]("""{ "type": "PRIVATE"}""")(ApiAccess.Private(false))
    }

    "write to Json" in {
      Json.toJson[ApiAccess](ApiAccess.Public) shouldBe Json.obj("type" -> "PUBLIC")
      Json.toJson[ApiAccess](ApiAccess.Internal) shouldBe Json.obj("type" -> "INTERNAL")
      Json.toJson[ApiAccess](ApiAccess.Controlled) shouldBe Json.obj("type" -> "CONTROLLED")
      Json.toJson[ApiAccess](ApiAccess.Private(false)) shouldBe Json.obj(
        ("type"    -> "PRIVATE"),
        ("isTrial" -> false)
      )
    }
  }
}
