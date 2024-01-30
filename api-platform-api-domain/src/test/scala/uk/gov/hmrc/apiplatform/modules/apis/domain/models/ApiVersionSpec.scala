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

import scala.util.Random

import play.api.libs.json.Json

import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiVersionNbr
import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class ApiVersionSpec extends BaseJsonFormattersSpec with ApiDefinitionFactory {

  val example = ApiVersion(
    versionNbr = ApiVersionNbr("1.0"),
    status = ApiStatus.STABLE,
    access = ApiAccess.PUBLIC,
    endpoints = List(Endpoint(
      uriPattern = "url",
      endpointName = "name",
      method = HttpMethod.GET,
      authType = AuthType.NONE,
      throttlingTier = ResourceThrottlingTier.UNLIMITED,
      scope = None,
      queryParameters = Nil
    )),
    endpointsEnabled = true,
    awsRequestId = None,
    versionSource = ApiVersionSource.OAS
  )

  val expectedJson =
    """{"version":"1.0","status":"STABLE","access":{"type":"PUBLIC"},"endpoints":[{"uriPattern":"url","endpointName":"name","method":"GET","authType":"NONE","throttlingTier":"UNLIMITED","queryParameters":[]}],"endpointsEnabled":true,"versionSource":"OAS"}"""

  "ApiVersion" should {
    val openEndpoint        = anEndpoint.copy(authType = AuthType.NONE)
    val applicationEndpoint = anEndpoint.copy(authType = AuthType.APPLICATION)
    val userEndpoint        = anEndpoint.copy(authType = AuthType.USER)

    "determine isOpenAccess when not all endpoints are open and public" in {
      val version = buildVersion("1.0", endpoints = List(openEndpoint, applicationEndpoint, userEndpoint))

      version.isOpenAccess shouldBe false
    }

    "determine isOpenAccess when all endpoints are open and public" in {
      val version = buildVersion("1.0", endpoints = List(openEndpoint, openEndpoint))

      version.isOpenAccess shouldBe true
    }

    "determine isOpenAccess when all endpoints are open but version is not public" in {
      val version = buildVersion("1.0", apiAccess = ApiAccess.Private(), endpoints = List(openEndpoint, openEndpoint))

      version.isOpenAccess shouldBe false
    }

    "read from Json" in {
      testFromJson(expectedJson)(example)
    }

    "read from Json with new field name" in {
      val newExpectedJson =
        """{"versionNbr":"1.0","status":"STABLE","access":{"type":"PUBLIC"},"endpoints":[{"uriPattern":"url","endpointName":"name","method":"GET","authType":"NONE","throttlingTier":"UNLIMITED","queryParameters":[]}],"endpointsEnabled":true,"versionSource":"OAS"}"""
      testFromJson(newExpectedJson)(example)
    }

    "write to Json" in {
      Json.toJson[ApiVersion](example).toString shouldBe expectedJson
    }

    "ordering" in {
      val nbrs     = List("1.0", "1.1", "2.0", "3.0", "4.0", "5.0", "5.1")
      val versions = nbrs.map(v => example.copy(versionNbr = ApiVersionNbr(v)))

      Random.shuffle(versions).sorted shouldBe versions
    }
  }
}
