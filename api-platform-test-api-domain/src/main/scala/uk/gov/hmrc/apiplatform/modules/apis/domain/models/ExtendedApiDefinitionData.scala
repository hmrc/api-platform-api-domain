/*
 * Copyright 2024 HM Revenue & Customs
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

import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiContextData

object ExtendedApiDefinitionData {

  val extendedApiDefinition = ExtendedApiDefinition(
    ServiceNameData.serviceName,
    "http://localhost",
    "Test API defintion name",
    "Test API defintion description",
    ApiContextData.contextA,
    List(
      ExtendedApiVersion(
        ApiVersionData.apiVersionOnePublicStable.versionNbr,
        ApiVersionData.apiVersionOnePublicStable.status,
        ApiVersionData.apiVersionOnePublicStable.endpoints,
        Some(
          ApiAvailability(
            true,
            ApiAccess.PUBLIC,
            false,
            false
          )
        ),
        Some(
          ApiAvailability(
            true,
            ApiAccess.PUBLIC,
            false,
            false
          )
        )
      )
    ),
    false,
    None,
    List(ApiCategory.EXAMPLE)
  )
}
