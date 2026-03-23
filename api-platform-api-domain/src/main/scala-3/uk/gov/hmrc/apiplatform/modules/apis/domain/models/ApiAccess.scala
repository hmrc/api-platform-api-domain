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

  lazy val isPublic = this == ApiAccess.Public
}

object ApiAccess {
  case object Public     extends ApiAccess
  case object Internal   extends ApiAccess
  case object Controlled extends ApiAccess

  case class Private(isTrial: Boolean = false) extends ApiAccess

  def displayText(apiAccess: ApiAccess): String = apiAccess.accessType.toString

  def accessType(apiAccess: ApiAccess) = apiAccess match {
    case Public     => ApiAccessType.Public
    case Internal   => ApiAccessType.Internal
    case Controlled => ApiAccessType.Controlled
    case Private(_) => ApiAccessType.Private
  }

  import play.api.libs.json.*
  import uk.gov.hmrc.play.json.Union

  private given OFormat[Private] = Json.format[Private]

  given Format[ApiAccess] = Union.from[ApiAccess]("type")
    .andType("PUBLIC", () => Public)
    .andType("INTERNAL", () => Internal)
    .andType("CONTROLLED", () => Controlled)
    .and[Private]("PRIVATE")
    .format
}
