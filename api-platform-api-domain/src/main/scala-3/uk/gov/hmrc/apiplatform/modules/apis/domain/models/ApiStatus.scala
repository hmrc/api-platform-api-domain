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

enum ApiStatus {
  case Alpha
  case Beta
  case Stable
  case Deprecated
  case Retired
}

object ApiStatus {
  def apply(text: String): Option[ApiStatus] = ApiStatus.values.find(_.toString.equalsIgnoreCase(text))

  def unsafeApply(text: String): ApiStatus = apply(text).getOrElse(throw new RuntimeException(s"$text is not a valid API Status"))

  private def priorityOf(apiStatus: ApiStatus): Int = {
    apiStatus match {
      case ApiStatus.Stable     => 1
      case ApiStatus.Beta       => 2
      case ApiStatus.Alpha      => 3
      case ApiStatus.Deprecated => 4
      case ApiStatus.Retired    => 5
    }
  }
  val orderingByPriority: Ordering[ApiStatus]       = Ordering.by[ApiStatus, Int](ApiStatus.priorityOf)

  given Ordering[ApiStatus] = Ordering.by[ApiStatus, Int](ApiStatus.values.indexOf(_))

  import play.api.libs.json.Format
  import uk.gov.hmrc.apiplatform.modules.common.domain.services.SimpleEnumJsonFormatting

  given Format[ApiStatus] = SimpleEnumJsonFormatting.createEnumFormatFor[ApiStatus]("API Status", apply)
}
