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

sealed trait ApiAccess {
  lazy val displayText: String       = ApiAccess.displayText(this)
  lazy val accessType: ApiAccessType = ApiAccess.accessType(this)

  lazy val isPublic = this == ApiAccess.PUBLIC
}

object ApiAccess {
  case object PUBLIC extends ApiAccess

  case class Private(isTrial: Boolean = false) extends ApiAccess

  def displayText(apiAccess: ApiAccess): String = apiAccess.accessType.displayText

  def accessType(apiAccess: ApiAccess) = apiAccess match {
    case PUBLIC     => ApiAccessType.PUBLIC
    case Private(_) => ApiAccessType.PRIVATE
  }

  import play.api.libs.json._
  import uk.gov.hmrc.play.json.Union

  private implicit val formatPrivateApiAccess: OFormat[Private] = Json.format[Private]

  implicit val format: Format[ApiAccess] = Union.from[ApiAccess]("type")
    .andType("PUBLIC", () => PUBLIC)
    .and[Private]("PRIVATE")
    .format
}
