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

import uk.gov.hmrc.apiplatform.modules.apis.domain.models.{ApiAccess, ApiCategory, ApiDefinition}
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

class ApiDefinitionSpec extends HmrcSpec {

  "ApiDefinition" should {
    def anApiDefinition(accessType: String = "PUBLIC", whitelistedApplicationIds: Option[String] = None, isTrial: Option[Boolean] = None, categories: Option[String] = None) = {

      val body =
        s"""{
           |   "serviceName":"calendar",
           |   "name":"Calendar API",
           |   "description":"My Calendar API",
           |   "serviceBaseUrl":"http://calendar",
           |   "context":"calendar",
           |   ${categories.fold("")(c => s""" "categories": $c,""")}
           |   "versions":[
           |      {
           |         "version":"1.0",
           |         "status":"STABLE",
           |         "access": {
           |           "type": "$accessType"
           |            ${whitelistedApplicationIds.fold("")(w => s""" ,"whitelistedApplicationIds": $w""")}
           |            ${isTrial.fold("")(t => s""","isTrial": $t""")}
           |         },
           |         "endpoints":[
           |            {
           |               "uriPattern":"/today",
           |               "endpointName":"Get Today's Date",
           |               "method":"GET",
           |               "authType":"NONE",
           |               "throttlingTier":"UNLIMITED"
           |            }
           |         ]
           |      }
           |   ]
           |}""".stripMargin.replaceAll("\n", " ")

      Json.parse(body).as[ApiDefinition]
    }

    "read from JSON when the API access type is PUBLIC and there is no whitelist" in {
      val apiDefinition = anApiDefinition()

      apiDefinition.versions.head.access shouldBe Some(ApiAccess.PUBLIC)
    }

    "read from JSON when the API access type is PUBLIC and there is an empty whitelist" in {
      val apiDefinition = anApiDefinition(whitelistedApplicationIds = Some("[]"))

      apiDefinition.versions.head.access shouldBe Some(ApiAccess.PUBLIC)
    }

    "read from JSON when the API access type is PRIVATE and there is an empty whitelist" in {
      val apiDefinition = anApiDefinition(
        accessType = "PRIVATE",
        whitelistedApplicationIds = Some("[]")
      )

      apiDefinition.versions.head.access shouldBe Some(ApiAccess.Private(Nil))
    }

    "read from JSON when the API access type is PRIVATE and there is an empty whitelist and isTrial is true" in {
      val apiDefinition = anApiDefinition(
        accessType = "PRIVATE",
        whitelistedApplicationIds = Some("[]"),
        isTrial = Some(true)
      )

      apiDefinition.versions.head.access shouldBe Some(ApiAccess.Private(Nil, Some(true)))
    }

    "read from JSON when the API access type is PRIVATE and there is a non-empty whitelist" in {
      val apiDefinition = anApiDefinition(accessType = "PRIVATE", whitelistedApplicationIds = Some("[\"an-application-id\"]"))

      apiDefinition.versions.head.access shouldBe Some(ApiAccess.Private(List("an-application-id")))
    }

    "no longer fail to read from JSON when the API access type is PRIVATE and there is no whitelist" in {
      val apiDefinition = anApiDefinition(accessType = "PRIVATE", whitelistedApplicationIds = None)

      apiDefinition.versions.head.access shouldBe Some(ApiAccess.Private(List.empty))
    }

    "read from JSON when the API categories are defined but empty" in {
      val apiDefinition = anApiDefinition(categories = Some("[]"))

      apiDefinition.categories shouldBe Some(Nil)
    }

    "read from JSON when the API categories are defined with correct values" in {
      val apiDefinition = anApiDefinition(categories = Some("[\"CUSTOMS\", \"VAT\"]"))

      apiDefinition.categories shouldBe Some(Seq(ApiCategory.CUSTOMS, ApiCategory.VAT))
    }

    "fail to read from JSON when the API categories are defined with incorrect values" in {
      intercept[RuntimeException] {
        anApiDefinition(categories = Some("[\"NOT_A_VALID_CATEGORY\"]"))
      }
    }
  }

}
