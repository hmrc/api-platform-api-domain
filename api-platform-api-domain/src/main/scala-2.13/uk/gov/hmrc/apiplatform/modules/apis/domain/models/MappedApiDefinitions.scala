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

import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiContext

// Wrapper class for endpoints returning Map[ApiContext,ApiDefintion] useful until such time as everything can switch to List[ApiDefinition]
//   but that will read a list if the supplier is updated first

final case class MappedApiDefinitions(wrapped: Map[ApiContext, ApiDefinition]) extends AnyVal

object MappedApiDefinitions {
  import play.api.libs.json._
  import play.api.libs.functional.syntax._ // Combinator syntax

  def fromList(in: List[ApiDefinition]): Map[ApiContext, ApiDefinition] = {
    in.map(defn => defn.context -> defn).toMap
  }

  implicit val readsWrapper: Reads[MappedApiDefinitions] = (
    (
      (JsPath).read[Map[ApiContext, ApiDefinition]] or
        (JsPath).read[List[ApiDefinition]].map(fromList)
    ).map(MappedApiDefinitions(_))
  )
}
