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

import uk.gov.hmrc.apiplatform.modules.common.domain.services.SealedTraitJsonFormatting

sealed trait HttpMethod

object HttpMethod {
  case object GET     extends HttpMethod
  case object POST    extends HttpMethod
  case object PUT     extends HttpMethod
  case object PATCH   extends HttpMethod
  case object DELETE  extends HttpMethod
  case object OPTIONS extends HttpMethod
  case object HEAD    extends HttpMethod

  final val values = Set[HttpMethod](GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD)

  def apply(text: String): Option[HttpMethod] = HttpMethod.values.find(_.toString == text.toUpperCase)

  def unsafeApply(text: String): HttpMethod = apply(text).getOrElse(throw new RuntimeException(s"$text is not a valid HTTP Method"))

  import play.api.libs.json.Format
  implicit val format: Format[HttpMethod] = SealedTraitJsonFormatting.createFormatFor[HttpMethod]("HTTP Method", apply)
}
