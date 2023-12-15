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

import uk.gov.hmrc.apiplatform.modules.common.domain.models.{ApiContext, ApiVersionNbr}

trait ApiDefinitionFactory {
  protected val anEndpoint = Endpoint("endpoint1", "/some/endpoint1", HttpMethod.POST, AuthType.USER)

  def buildVersion(version: String, status: ApiStatus = ApiStatus.STABLE, apiAccess: ApiAccess = ApiAccess.PUBLIC, endpoints: List[Endpoint] = List(anEndpoint)): ApiVersion = {
    ApiVersion(ApiVersionNbr(version), status, apiAccess, endpoints)
  }

  def buildExtendedVersion(
      version: String,
      status: ApiStatus = ApiStatus.STABLE,
      endpoints: List[Endpoint] = List(anEndpoint),
      productionAvailability: Option[ApiAvailability] = Some(ApiAvailability(endpointsEnabled = true, access = ApiAccess.PUBLIC, loggedIn = true, authorised = true)),
      sandboxAvailability: Option[ApiAvailability] = Some(ApiAvailability(endpointsEnabled = true, access = ApiAccess.PUBLIC, loggedIn = true, authorised = true))
    ): ExtendedApiVersion = {
    ExtendedApiVersion(ApiVersionNbr(version), status, endpoints, productionAvailability, sandboxAvailability)
  }

  def buildDefinition(versions: List[ApiVersion]): ApiDefinition = {
    ApiDefinition(
      ServiceName("test1ServiceName"),
      "someUrl",
      "test1Name",
      "test1Desc",
      ApiContext("som/context/here"),
      versions.groupBy(_.versionNbr).map { case (k, vs) => k -> vs.head },
      isTestSupport = false,
      None,
      List.empty
    )
  }

  def buildExtendedDefinition(versions: List[ExtendedApiVersion]): ExtendedApiDefinition = {
    ExtendedApiDefinition(
      serviceName = ServiceName("test1ServiceName"),
      serviceBaseUrl = "someUrl",
      name = "test1Name",
      description = "test1Desc",
      context = ApiContext("som/context/here"),
      versions = versions,
      isTestSupport = false,
      lastPublishedAt = None,
      categories = List.empty
    )
  }
}
