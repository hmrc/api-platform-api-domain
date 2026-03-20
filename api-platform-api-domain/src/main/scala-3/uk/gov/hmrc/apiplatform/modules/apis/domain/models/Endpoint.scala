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

case class Endpoint(
    uriPattern: Endpoint.UriPattern,
    endpointName: Endpoint.Name,
    method: HttpMethod,
    authType: AuthType,
    throttlingTier: ResourceThrottlingTier = ResourceThrottlingTier.Unlimited,
    scope: Option[Scope],
    queryParameters: List[QueryParameter]
  ) {

  def decoratedUriPattern = {
    if (!queryParameters.exists(_.required)) {
      uriPattern
    } else {
      val queryString = queryParameters
        .filter(_.required)
        .map(parameter => s"${parameter.name}={${parameter.name}}")
        .mkString("&")
      s"$uriPattern?$queryString"
    }
  }
}

object Endpoint {
  import play.api.libs.json.*

  opaque type Name <: String = String

  object Name {

    def apply(s: String): Name = s

    given Format[Name] = Format(Reads.StringReads, Writes.StringWrites)
  }

  opaque type UriPattern <: String = String

  object UriPattern {

    def apply(s: String): UriPattern = s

    given Format[UriPattern] = Format(Reads.StringReads, Writes.StringWrites)
  }

  given OFormat[Endpoint] = Json.format[Endpoint]
}
