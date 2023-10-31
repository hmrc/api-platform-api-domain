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

import java.time.Instant

import uk.gov.hmrc.apiplatform.modules.apis.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.domain.services.InstantJsonFormatter

case class ExtendedApiDefinition(
    serviceName: ServiceName,
    serviceBaseUrl: String,
    name: String,
    description: String,
    context: ApiContext,
    versions: List[ExtendedApiVersion],
    requiresTrust: Boolean,
    isTestSupport: Boolean,
    lastPublishedAt: Option[Instant],
    categories: List[ApiCategory] = List.empty
  ) {

  def userAccessibleApiDefinition = {
    copy(versions =
      versions.filter(v =>
        v.productionAvailability.exists(_.isAccessible) || v.sandboxAvailability.exists(_.isAccessible)
      )
    )
  }

  private val statusThenVersionOrdering: Ordering[ExtendedApiVersion] = Ordering.by[ExtendedApiVersion, ApiStatus](_.status)(ApiStatus.orderingByPriority).reverse
    .orElseBy(_.version).reverse

  lazy val sortedActiveVersions = versions
    .filterNot(_.status == ApiStatus.RETIRED)
    .sortBy(_.version).reverse

  lazy val defaultVersion = versions
    .filterNot(_.status == ApiStatus.RETIRED)
    .sorted(statusThenVersionOrdering)
    .headOption
}

object ExtendedApiDefinition {
  import play.api.libs.json.Json
  import InstantJsonFormatter.WithTimeZone._
  implicit val format = Json.format[ExtendedApiDefinition]
}
