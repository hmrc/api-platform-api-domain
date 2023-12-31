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

case class ApiAvailability(endpointsEnabled: Boolean, access: ApiAccess, loggedIn: Boolean, authorised: Boolean) {
  lazy val isAccessible: Boolean = authorised || access == ApiAccess.Private(true) || access == ApiAccess.PUBLIC
}

object ApiAvailability {
  import play.api.libs.json.{Format, Json}

  implicit val format: Format[ApiAvailability] = Json.format[ApiAvailability]
}
