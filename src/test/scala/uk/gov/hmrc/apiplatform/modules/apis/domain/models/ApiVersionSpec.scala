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

import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec
import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiVersionNbr
import play.api.libs.json.Json

class ApiVersionSpec extends BaseJsonFormattersSpec {

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
  val expectedJson = """{"version":"1.0","status":"STABLE","access":{"type":"PUBLIC"},"endpoints":[{"uriPattern":"url","endpointName":"name","method":"GET","authType":"NONE","throttlingTier":"UNLIMITED","queryParameters":[]}],"endpointsEnabled":true,"versionSource":"OAS"}"""


  "ApiVersion" should {
    "read from Json" in {
      testFromJson(expectedJson)(example)
    }

    "read from Json with new field name" in {
      val newExpectedJson = """{"versionNbr":"1.0","status":"STABLE","access":{"type":"PUBLIC"},"endpoints":[{"uriPattern":"url","endpointName":"name","method":"GET","authType":"NONE","throttlingTier":"UNLIMITED","queryParameters":[]}],"endpointsEnabled":true,"versionSource":"OAS"}"""
      testFromJson(newExpectedJson)(example)
    }

    "write to Json" in {
      Json.toJson[ApiVersion](example).toString shouldBe expectedJson
    }
  }
}