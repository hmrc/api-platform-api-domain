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
import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiContext
import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

class ApiDataSpec extends HmrcSpec {

  "ApiData" should {
    def apiDataJson(requiresTrust: Boolean = false, isTestSupport: Boolean = false, categories: String = """[ "AGENTS" ]""") = {
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

    def anApiData(requiresTrust: Boolean = false, isTestSupport: Boolean = false, categories: String = """[ "AGENTS" ]""") = {

      val body: String = apiDataJson(requiresTrust, isTestSupport, categories)

      Json.parse(body).as[ApiData]
    }

    val apiDefinitionJson =
      s"""{
         |   "serviceName":"calendar",
         |   "name":"Calendar API",
         |   "description":"My Calendar API",
         |   "serviceBaseUrl":"http://calendar",
         |   "context":"calendar",
         |   "categories": [ "AGENTS" ],
         |   "lastPublishedAt": "2011-12-03T10:15:30Z",
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

    def anApiDefinition() = {
      Json.parse(apiDefinitionJson).as[ApiDefinition]
    }

    "read from JSON" in {
      anApiData()
    }

    "write to JSON" in {
      Json.toJson(anApiData()) shouldBe Json.parse(apiDataJson())
    }

    "convert from apiDefinition" in {
      ApiData.from(anApiDefinition()) shouldBe anApiData()
    }

    "convert from a list of apiDefinitions" in {
      ApiData.from(List(anApiDefinition(), anApiDefinition())) shouldBe Map(ApiContext("calendar") -> anApiData())
    }

    "read from JSON when the API categories are defined with correct values" in {
      val apiData = anApiData(categories = """ [ "CUSTOMS", "VAT" ] """)

      apiData.categories shouldBe Seq(ApiCategory.CUSTOMS, ApiCategory.VAT)
    }

    "read from JSON when the API categories are defined but empty (this should fail publisher validation)" in {
      anApiData(categories = "[]")
    }

    "fail to read from JSON when the API categories are defined with incorrect values" in {
      intercept[RuntimeException] {
        anApiData(categories = """ [ "NOT_A_VALID_CATEGORY" ] """)
      }
    }
  }
}
