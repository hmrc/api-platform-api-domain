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
import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiContext
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

class ApiDefinitionSpec extends HmrcSpec {

  "ApiDefinition2" should {
    def aStoredApiDefinitionJson(
        requiresTrust: Boolean = false,
        isTestSupport: Boolean = false,
        categories: String = """[ "AGENTS" ]"""
      ): String = {

      s"""{
         |   "serviceName":"calendar",
         |   "name":"Calendar API",
         |   "description":"My Calendar API",
         |   "serviceBaseUrl":"http://calendar",
         |   "context":"calendar",
         |   "categories": $categories,
         |   "requiresTrust": $requiresTrust,
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
    }

    def anApiDefinitionJson(
        requiresTrust: Boolean = false,
        isTestSupport: Boolean = false,
        categories: String = """[ "AGENTS" ]"""
      ): String = {

      s"""{
         |   "serviceName":"calendar",
         |   "serviceBaseUrl":"http://calendar",
         |   "name":"Calendar API",
         |   "description":"My Calendar API",
         |   "context":"calendar",
         |   "versions":{
         |    "1.0":  {
         |         "version":"1.0",
         |         "status":"STABLE",
         |         "access": {
         |           "isTrial": false,
         |           "type": "PRIVATE"
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
         |          ],
         |         "endpointsEnabled": true,
         |         "versionSource": "OAS"
         |      }
         |   },
         |   "requiresTrust": $requiresTrust,
         |   "isTestSupport": $isTestSupport,
         |   "lastPublishedAt": "2011-12-03T10:15:30.000Z",
         |   "categories": $categories
         |}""".stripMargin.replaceAll("\n", " ")
    }

    "read from old style JSON" in {
      Json.parse(anApiDefinitionJson()).as[ApiDefinition]
    }

    "read from new style JSON" in {
      Json.parse(aStoredApiDefinitionJson()).as[ApiDefinition]
    }

    "write to JSON" in {
      val jsonText                     = anApiDefinitionJson()
      val apiDefinition: ApiDefinition = Json.parse(jsonText).as[ApiDefinition]

      Json.toJson(apiDefinition) shouldBe Json.parse(jsonText)
    }

    "convert from StoredApiDefinition" in {
      val storedApiDefinition: StoredApiDefinition = Json.parse(aStoredApiDefinitionJson()).as[StoredApiDefinition]
      val apiDefinition: ApiDefinition             = Json.parse(aStoredApiDefinitionJson()).as[ApiDefinition]

      ApiDefinition.fromStored(storedApiDefinition) shouldBe apiDefinition
    }

    "convert from a list of StoredApiDefinitions" in {
      val storedApiDefinition: StoredApiDefinition = Json.parse(aStoredApiDefinitionJson()).as[StoredApiDefinition]
      val apiDefinition: ApiDefinition             = Json.parse(aStoredApiDefinitionJson()).as[ApiDefinition]

      ApiDefinition.fromStoredCollection(List(storedApiDefinition)) shouldBe Map(ApiContext("calendar") -> apiDefinition)
    }

    "read from JSON when the API categories are defined with correct values" in {
      val apiDefinition = Json.parse(anApiDefinitionJson(categories = """ [ "CUSTOMS", "VAT" ] """)).as[ApiDefinition]

      apiDefinition.categories shouldBe Seq(ApiCategory.CUSTOMS, ApiCategory.VAT)
    }

    "read from JSON when the API categories are defined but empty (this should fail publisher validation)" in {
      val apiDefinition = Json.parse(anApiDefinitionJson(categories = """ [] """)).as[ApiDefinition]

      apiDefinition.categories shouldBe Seq()
    }

    "fail to read from JSON when the API categories are defined with incorrect values" in {
      intercept[RuntimeException] {
        Json.parse(anApiDefinitionJson(categories = """ [ "NOT_A_VALID_CATEGORY" ] """)).as[ApiDefinition]
      }
    }
  }
}
