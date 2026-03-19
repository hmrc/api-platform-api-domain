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
  protected val anEndpoint = Endpoint(Endpoint.UriPattern("/some/endpoint1"), Endpoint.Name("endpoint1"), HttpMethod.Post, AuthType.User)

  def buildVersion(version: String, status: ApiStatus = ApiStatus.Stable, apiAccess: ApiAccess = ApiAccess.Public, endpoints: List[Endpoint] = List(anEndpoint)): ApiVersion = {
    ApiVersion(ApiVersionNbr(version), status, apiAccess, endpoints)
  }

  def buildExtendedVersion(
      version: String,
      status: ApiStatus = ApiStatus.Stable,
      endpoints: List[Endpoint] = List(anEndpoint),
      productionAvailability: Option[ApiAvailability] = Some(ApiAvailability(endpointsEnabled = true, access = ApiAccess.Public, loggedIn = true, authorised = true)),
      sandboxAvailability: Option[ApiAvailability] = Some(ApiAvailability(endpointsEnabled = true, access = ApiAccess.Public, loggedIn = true, authorised = true))
    ): ExtendedApiVersion = {
    ExtendedApiVersion(ApiVersionNbr(version), status, endpoints, productionAvailability, sandboxAvailability)
  }

  def buildDefinition(versions: List[ApiVersion]): ApiDefinition = {
    ApiDefinition(
      ServiceName("test1ServiceName"),
      ApiDefinition.ServiceBaseUrl("someUrl"),
      ApiDefinition.Name("test1Name"),
      ApiDefinition.Description("test1Desc"),
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
      ApiDefinition.ServiceBaseUrl("someUrl"),
      ApiDefinition.Name("test1Name"),
      ApiDefinition.Description("test1Desc"),
      context = ApiContext("som/context/here"),
      versions = versions,
      isTestSupport = false,
      lastPublishedAt = None,
      categories = List.empty
    )
  }
}
