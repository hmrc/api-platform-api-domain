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

import uk.gov.hmrc.apiplatform.modules.apis.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiVersionNbr

case class ExtendedApiVersion(
    version: ApiVersionNbr,
    status: ApiStatus,
    endpoints: List[Endpoint],
    productionAvailability: Option[ApiAvailability],
    sandboxAvailability: Option[ApiAvailability]
  ) {

  val displayedStatus = {
    val accessIndicator = sandboxAvailability.orElse(productionAvailability).map(_.access) match {
      case Some(ApiAccess.Private(_)) => "Private "
      case _                          => ""
    }
    s"${accessIndicator}${status.displayText}"
  }
}

object ExtendedApiVersion {
  import play.api.libs.json.Json
  implicit val format = Json.format[ExtendedApiVersion]
}
