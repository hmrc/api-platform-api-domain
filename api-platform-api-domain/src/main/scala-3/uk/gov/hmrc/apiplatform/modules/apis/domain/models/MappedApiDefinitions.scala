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

opaque type MappedApiDefinitions <: Map[ApiContext, ApiDefinition] = Map[ApiContext, ApiDefinition]

private object BlindReaders {
  import play.api.libs.json.*
  val readFromMap: Reads[Map[ApiContext, ApiDefinition]] = summon[Reads[Map[ApiContext, ApiDefinition]]]

  private def fromList(in: List[ApiDefinition]): Map[ApiContext, ApiDefinition] = {
    in.map(defn => defn.context -> defn).toMap
  }
  val readFromList: Reads[Map[ApiContext, ApiDefinition]]                       = summon[Reads[List[ApiDefinition]]].map(fromList)
}

object MappedApiDefinitions {
  def apply(m: Map[ApiContext, ApiDefinition]): MappedApiDefinitions = m

  import play.api.libs.json.*
  import BlindReaders.*
  given Reads[MappedApiDefinitions] = (readFromMap orElse readFromList).map(MappedApiDefinitions.apply)
}
