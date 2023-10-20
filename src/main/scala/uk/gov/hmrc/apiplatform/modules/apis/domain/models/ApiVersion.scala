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
import uk.gov.hmrc.apiplatform.modules.common.domain.models._

case class ApiVersion(
    versionNbr: ApiVersionNbr,
    status: ApiStatus,
    access: ApiAccess = ApiAccess.PUBLIC,
    endpoints: List[Endpoint], // Should be NonEmpty
    endpointsEnabled: Boolean = true,
    awsRequestId: Option[String] = None,
    versionSource: ApiVersionSource = ApiVersionSource.UNKNOWN
  ) {
  // No endpoints must have an auth type and version must be public
  lazy val isOpenAccess: Boolean = access.isPublic && endpoints.find(_.authType != AuthType.NONE).isEmpty
}

object ApiVersion {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  private val reads: Reads[ApiVersion] = (
    (
      (JsPath \ "version").read[ApiVersionNbr] or   // Existing field name
        (JsPath \ "versionNbr").read[ApiVersionNbr] // TODO - Future aim to be this field name
    ) and
      (JsPath \ "status").read[ApiStatus] and
      (JsPath \ "access").read[ApiAccess] and
      (JsPath \ "endpoints").read[List[Endpoint]] and
      (JsPath \ "endpointsEnabled").read[Boolean] and
      (JsPath \ "awsRequestId").readNullable[String] and
      (JsPath \ "versionSource").read[ApiVersionSource]
  )(ApiVersion.apply _)

  private val writes: OWrites[ApiVersion] = (
    (JsPath \ "version").write[ApiVersionNbr] and // TODO - change to versionNbr once all readers are safe
      (JsPath \ "status").write[ApiStatus] and
      (JsPath \ "access").write[ApiAccess] and
      (JsPath \ "endpoints").write[List[Endpoint]] and
      (JsPath \ "endpointsEnabled").write[Boolean] and
      (JsPath \ "awsRequestId").writeNullable[String] and
      (JsPath \ "versionSource").write[ApiVersionSource]
  )(unlift(ApiVersion.unapply))

  implicit val ordering: Ordering[ApiVersion] = Ordering.by[ApiVersion, ApiVersionNbr](_.versionNbr)

  implicit val format: OFormat[ApiVersion] = OFormat(reads, writes)
}

