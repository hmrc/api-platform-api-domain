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

import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApplicationId

sealed trait ApiAccess {
  lazy val displayText: String       = ApiAccess.displayText(this)
  lazy val accessType: ApiAccessType = ApiAccess.accessType(this)
}

object ApiAccess {
  case object PUBLIC extends ApiAccess

  case class Private(allowlistedApplicationIds: List[ApplicationId], isTrial: Boolean = false) extends ApiAccess

  def displayText(apiAccess: ApiAccess): String = apiAccess match {
    case PUBLIC        => "Public"
    case Private(_, _) => "Private"
  }

  def accessType(apiAccess: ApiAccess) = apiAccess match {
    case PUBLIC        => ApiAccessType.PUBLIC
    case Private(_, _) => ApiAccessType.PRIVATE
  }

  import play.api.libs.json._
  import play.api.libs.functional.syntax._
  import uk.gov.hmrc.play.json.Union

  private val readsPrivateApiAccess: Reads[Private] = (
    (
      (JsPath \ "whitelistedApplicationIds").read[List[ApplicationId]] or // Existing field name
        (JsPath \ "allowlistedApplicationIds").read[List[ApplicationId]]  // TODO - Future aim to be this field name
    ) and
      (JsPath \ "isTrial").read[Boolean]
  )(Private.apply _)

  private val writesPrivateApiAccess: OWrites[Private] = (
    (JsPath \ "whitelistedApplicationIds").write[List[ApplicationId]] and // TODO - change to allowlisted once all readers are safe
      (JsPath \ "isTrial").write[Boolean]
  )(unlift(Private.unapply))

  private implicit val formatPublicApiAccess                    = Json.format[PUBLIC.type]
  private implicit val formatPrivateApiAccess: OFormat[Private] = OFormat[Private](readsPrivateApiAccess, writesPrivateApiAccess)

  implicit val formatApiAccess: Format[ApiAccess] = Union.from[ApiAccess]("type")
    .and[PUBLIC.type]("PUBLIC")
    .and[Private]("PRIVATE")
    .format
}
