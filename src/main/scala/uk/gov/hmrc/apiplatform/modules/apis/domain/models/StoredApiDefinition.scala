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

import uk.gov.hmrc.apiplatform.modules.common.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.domain.services.InstantJsonFormatter

case class StoredApiDefinition(
    serviceName: ServiceName,
    serviceBaseUrl: String,
    name: String,
    description: String,
    context: ApiContext,
    versions: List[ApiVersion],              // Should be NonEmpty
    requiresTrust: Boolean = false,          // Should be removed
    isTestSupport: Boolean = false,
    lastPublishedAt: Option[Instant] = None, // Only None in very old records from APIs that have not been published since field was added
    categories: List[ApiCategory]            // Should be NonEmpty
  )

object StoredApiDefinition {
  import play.api.libs.json._
  import InstantJsonFormatter.WithTimeZone._
  import play.api.libs.functional.syntax._ // Combinator syntax

  val reads: Reads[StoredApiDefinition] = (
    (JsPath \ "serviceName").read[ServiceName] and
      (JsPath \ "serviceBaseUrl").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "context").read[ApiContext] and
      (JsPath \ "versions").read[List[ApiVersion]] and
      ((JsPath \ "requiresTrust").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isTestSupport").read[Boolean] or Reads.pure(false)) and
      (JsPath \ "lastPublishedAt").readNullable[Instant] and
      (JsPath \ "categories").read[List[ApiCategory]]
  )(StoredApiDefinition.apply _)

  val writes: OWrites[StoredApiDefinition] = Json.writes[StoredApiDefinition]

  implicit val api: OFormat[StoredApiDefinition] = OFormat[StoredApiDefinition](reads, writes)
}
