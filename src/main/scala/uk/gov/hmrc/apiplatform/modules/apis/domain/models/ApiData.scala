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

import java.time.Instant

case class ApiData(
    serviceName: ServiceName,
    serviceBaseUrl: String,
    name: String,
    description: String,
    context: ApiContext,
    versions: Map[ApiVersionNbr, ApiVersion],
    requiresTrust: Boolean = false,
    isTestSupport: Boolean = false,
    lastPublishedAt: Option[Instant] = None,
    categories: List[ApiCategory]
  )

object ApiData {
  import play.api.libs.json._
  import uk.gov.hmrc.apiplatform.modules.common.domain.services.InstantJsonFormatter.WithTimeZone._
  import play.api.libs.functional.syntax._ // Combinator syntax

  val reads: Reads[ApiData] = (
    (JsPath \ "serviceName").read[ServiceName] and
      (JsPath \ "serviceBaseUrl").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "context").read[ApiContext] and
      (JsPath \ "versions").read[Map[ApiVersionNbr, ApiVersion]] and
      ((JsPath \ "requiresTrust").read[Boolean] or Reads.pure(false)) and
      ((JsPath \ "isTestSupport").read[Boolean] or Reads.pure(false)) and
      (JsPath \ "lastPublishedAt").readNullable[Instant] and
      (JsPath \ "categories").read[List[ApiCategory]]
  )(ApiData.apply _)

  val writes: OWrites[ApiData]                 = Json.writes[ApiData]
  implicit val formatApiData: OFormat[ApiData] = OFormat[ApiData](reads, writes)

  type ApiDefinitionMap = Map[ApiContext, ApiData]

  def from(in: List[ApiDefinition]): ApiDefinitionMap = {
    in.map(definition => definition.context -> from(definition)).toMap
  }

  def fromVersions(in: List[ApiVersion]): Map[ApiVersionNbr, ApiVersion] = {
    in.map(version => version.versionNbr -> version).toMap
  }

  def from(in: ApiDefinition): ApiData = {
    ApiData(
      in.serviceName,
      in.serviceBaseUrl,
      in.name,
      in.description,
      in.context,
      fromVersions(in.versions),
      in.requiresTrust,
      in.isTestSupport,
      in.lastPublishedAt,
      in.categories
    )
  }
}
