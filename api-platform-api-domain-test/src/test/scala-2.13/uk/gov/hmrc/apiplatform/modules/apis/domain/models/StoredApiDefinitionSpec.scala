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

import uk.gov.hmrc.apiplatform.modules.apis.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

class StoredApiDefinitionSpec extends HmrcSpec {

  "StoredApiDefinition" should {
    def anApiDefinition(
        accessType: String = "PUBLIC",
        isTrial: Boolean = false,
        isTestSupport: Boolean = false,
        categories: String = """[ "AGENTS" ]"""
      ) = {

      val body =
        s"""{
           |   "serviceName":"calendar",
           |   "name":"Calendar API",
           |   "description":"My Calendar API",
           |   "serviceBaseUrl":"http://calendar",
           |   "context":"calendar",
           |   "categories": $categories,
           |   "isTestSupport": $isTestSupport,
           |   "versions":[
           |      {
           |         "version":"1.0",
           |         "status":"STABLE",
           |         "endpointsEnabled": true,
           |         "versionSource": "OAS",
           |         "access": {
           |           "type": "PRIVATE",
           |           "whitelistedApplicationIds": [],
           |           "isTrial": false
           |         },
           |         "endpoints":[
           |            {
           |               "uriPattern":"/today",
           |               "endpointName":"Get Today's Date",
           |               "method":"GET",
           |               "authType":"NONE",
           |               "throttlingTier":"UNLIMITED",
           |               "queryParameters": []
           |            }
           |         ]
           |      }
           |   ]
           |}""".stripMargin.replaceAll("\n", " ")

      Json.parse(body).as[ApiDefinition]
    }

    "read from JSON" in {
      anApiDefinition()
    }

    "read from JSON when the API categories are defined with correct values" in {
      val apiDefinition = anApiDefinition(categories = """ [ "CUSTOMS", "VAT" ] """)

      apiDefinition.categories shouldBe Seq(ApiCategory.CUSTOMS, ApiCategory.VAT)
    }

    "read from JSON when the API categories are defined but empty (this should fail publisher validation)" in {
      anApiDefinition(categories = "[]")
    }

    "fail to read from JSON when the API categories are defined with incorrect values" in {
      intercept[RuntimeException] {
        anApiDefinition(categories = """ [ "NOT_A_VALID_CATEGORY" ] """)
      }
    }
  }

}
