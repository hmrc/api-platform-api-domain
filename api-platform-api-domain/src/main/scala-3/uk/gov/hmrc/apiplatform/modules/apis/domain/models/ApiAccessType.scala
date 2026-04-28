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

import uk.gov.hmrc.apiplatform.modules.common.domain.services.SimpleEnumJsonFormatting

enum ApiAccessType {
  case Public, Internal, Controlled

  def isPublic = this == ApiAccessType.Public
}

object ApiAccessType {
  def apply(text: String): Option[ApiAccessType] = ApiAccessType.values.find(_.toString().equalsIgnoreCase(text))

  def unsafeApply(text: String): ApiAccessType = apply(text).getOrElse(throw new RuntimeException(s"$text is not a valid API Access Type"))

  import play.api.libs.json._
  val simpleFormat: Format[ApiAccessType] = SimpleEnumJsonFormatting.createStringFormatFor[ApiAccessType]("API Access Type", apply, _.toString.toUpperCase)

  import cats.implicits._

  // This can be removed once all data is in the new format.
  given Reads[ApiAccessType] = simpleFormat.preprocess {
    case JsString(x)                                                                                                   => JsString(x)
    case JsObject(fields) if (fields.get("type") == JsString("PUBLIC").some)                                           => JsString("PUBLIC")
    case JsObject(fields) if (fields.get("type") == JsString("INTERNAL").some)                                         => JsString("INTERNAL")
    case JsObject(fields) if (fields.get("type") == JsString("CONTROLLED").some)                                       => JsString("CONTROLLED")
    case JsObject(fields) if (fields.get("type") == JsString("PRIVATE").some && fields.get("isTrial").isEmpty)         => JsString("INTERNAL")
    case JsObject(fields) if (fields.get("type") == JsString("PRIVATE").some && fields.get("isTrial") == JsNull.some)  => JsString("INTERNAL")
    case JsObject(fields) if (fields.get("type") == JsString("PRIVATE").some && fields.get("isTrial") == JsFalse.some) => JsString("INTERNAL")
    case JsObject(fields) if (fields.get("type") == JsString("PRIVATE").some && fields.get("isTrial") == JsTrue.some)  => JsString("CONTROLLED")
  }

  given Writes[ApiAccessType] = simpleFormat

}
