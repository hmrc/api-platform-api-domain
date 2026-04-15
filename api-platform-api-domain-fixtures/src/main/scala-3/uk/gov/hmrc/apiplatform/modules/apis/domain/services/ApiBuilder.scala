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

package uk.gov.hmrc.apiplatform.modules.apis.domain.services

import uk.gov.hmrc.apiplatform.modules.apis.domain.models.*
import uk.gov.hmrc.apiplatform.modules.common.domain.models.{ApiContext, ApiVersionNbr, ApiVersionNbrFixtures}
import uk.gov.hmrc.apiplatform.modules.common.services.ClockNow

trait ApiBuilder extends ApiVersionNbrFixtures with ClockNow {

  extension (versionData: ApiVersion) {
    def withStatus(newStatus: ApiStatus) = versionData.copy(status = newStatus)
    def alpha                            = versionData.copy(status = ApiStatus.Alpha)
    def beta                             = versionData.copy(status = ApiStatus.Beta)
    def stable                           = versionData.copy(status = ApiStatus.Stable)
    def deprecated                       = versionData.copy(status = ApiStatus.Deprecated)
    def retired                          = versionData.copy(status = ApiStatus.Retired)

    def withAccess(newAccess: ApiAccessType) = versionData.copy(access = newAccess)
    def publicAccess                         = versionData.withAccess(ApiAccessType.Public)
    def controlledAccess                     = versionData.withAccess(ApiAccessType.Controlled)
    def internalAccess                       = versionData.withAccess(ApiAccessType.Internal)
  }

  extension (apiDefinition: ApiDefinition) {
    def testSupport = apiDefinition.copy(isTestSupport = true)

    def withName(newName: String) = apiDefinition.copy(name = ApiDefinition.Name(newName))

    def withVersion(versionNbr: ApiVersionNbr, data: ApiVersion = DefaultVersionData) = apiDefinition.copy(versions = Map(versionNbr -> data))

    def addVersion(versionNbr: ApiVersionNbr, data: ApiVersion = DefaultVersionData) = apiDefinition.copy(versions = apiDefinition.versions + (versionNbr -> data))

    def withContext(apiContext: ApiContext) = apiDefinition.copy(context = apiContext)
  }

  val DefaultVersionData = ApiVersion(apiVersionNbrOne, ApiStatus.Stable, ApiAccessType.Public, List.empty, awsRequestId = None, endpointsEnabled = true)

  val DefaultServiceName = ServiceName("A-Service")
  val DefaultName        = ApiDefinition.Name("API Name")

  val DefaultApiDefinition = ApiDefinition(
    serviceName = DefaultServiceName,
    serviceBaseUrl = ApiDefinition.ServiceBaseUrl("http://serviceBaseUrl"),
    name = DefaultName,
    description = ApiDefinition.Description("Description"),
    context = ApiContext("context/name"),
    versions = Map(apiVersionNbrOne -> DefaultVersionData),
    isTestSupport = false,
    lastPublishedAt = Some(instant),
    categories = List.empty
  )
}
