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

import play.api.libs.json._

import uk.gov.hmrc.apiplatform.modules.common.utils.{ApiBuilder, HmrcSpec}

class MappedApiDefinitionsSpec extends HmrcSpec with ApiBuilder {

  "return api definitions as mapped api definitions when Json is a map" in {
    val apiDefinition           = DefaultApiDefinition.addVersion(VersionOne, DefaultVersionData)
    val apiContext              = apiDefinition.context
    val apiContextAndDefinition = Map(apiContext -> apiDefinition)
    val payload: JsValue        = Json.toJson(apiContextAndDefinition)

    val result = Json.fromJson[MappedApiDefinitions](payload).asOpt
    result.value.wrapped shouldBe Map(apiContext -> apiDefinition)
  }

  "return api definitions as mapped api definitions when Json is a list" in {
    val apiDefinition    = DefaultApiDefinition.addVersion(VersionOne, DefaultVersionData)
    val apiContext       = apiDefinition.context
    val payload: JsValue = Json.toJson(List(apiDefinition))

    val result = Json.fromJson[MappedApiDefinitions](payload).asOpt
    result.value.wrapped shouldBe Map(apiContext -> apiDefinition)
  }
}
