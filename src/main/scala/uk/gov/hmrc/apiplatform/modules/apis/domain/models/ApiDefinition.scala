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

case class ApiDefinition(
    serviceName: ServiceName,
    serviceBaseUrl: String,
    name: String,
    description: String,
    context: ApiContext,
    versions: Map[ApiVersionNbr, ApiVersion], // Should be NonEmpty
    requiresTrust: Boolean = false,           // Should be removed
    isTestSupport: Boolean = false,
    lastPublishedAt: Option[Instant] = None,  // Only None in very old records from APIs that have not been published since field was added
    categories: List[ApiCategory]             // Should be NonEmpty
  ) {

  lazy val versionsAsList: List[ApiVersion] = versions.values.toList

  def filterVersions(fn: ApiVersions.ApiVersionFilterFn): Option[ApiDefinition] = {
    val filteredVersions = versions.filter(kv => fn(kv._2))
    if (filteredVersions.isEmpty) None else Some(copy(versions = filteredVersions))
  }

  // All versions must be open access
  lazy val isOpenAccess: Boolean = versions.values.find(_.isOpenAccess == false).isEmpty
}

object ApiDefinition {
  
  import play.api.libs.json._
  import InstantJsonFormatter.WithTimeZone._
  import play.api.libs.functional.syntax._ // Combinator syntax

  val reads: Reads[ApiDefinition] = (
    (JsPath \ "serviceName").read[ServiceName] and
      (JsPath \ "serviceBaseUrl").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "context").read[ApiContext] and
      (
        (JsPath \ "versions").read[Map[ApiVersionNbr, ApiVersion]] or
          (JsPath \ "versions").read[List[ApiVersion]].map(ApiDefinition.fromStoredVersions)
      ) and
      ((JsPath \ "requiresTrust").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isTestSupport").read[Boolean] or Reads.pure(false)) and
      (JsPath \ "lastPublishedAt").readNullable[Instant] and
      (JsPath \ "categories").read[List[ApiCategory]]
  )(ApiDefinition.apply _)

  val writes: OWrites[ApiDefinition] = Json.writes[ApiDefinition]

  implicit val api: OFormat[ApiDefinition] = OFormat[ApiDefinition](reads, writes)

  def fromStoredCollection(in: List[StoredApiDefinition]): Map[ApiContext, ApiDefinition] = {
    in.map(definition => definition.context -> fromStored(definition)).toMap
  }

  def fromStoredVersions(in: List[ApiVersion]): Map[ApiVersionNbr, ApiVersion] = {
    in.map(version => version.versionNbr -> version).toMap
  }

  def fromStored(in: StoredApiDefinition): ApiDefinition = {
    ApiDefinition(
      in.serviceName,
      in.serviceBaseUrl,
      in.name,
      in.description,
      in.context,
      fromStoredVersions(in.versions),
      in.requiresTrust,
      in.isTestSupport,
      in.lastPublishedAt,
      in.categories
    )
  }
}
