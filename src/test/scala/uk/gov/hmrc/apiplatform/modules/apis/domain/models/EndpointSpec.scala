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

import org.scalatest.prop.TableDrivenPropertyChecks
import play.api.libs.json.{JsString, Json}
import uk.gov.hmrc.apiplatform.modules.common.utils._

class EndpointSpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks {

  "decoratedUriPattern" should {
    case class Scenario(outputUriPattern: String, inputUriPattern: String, inputParameters: List[QueryParameter] = List.empty)

    val mandatory        = QueryParameter("mandatory", required = true)
    val optional         = QueryParameter("optional")
    val anotherMandatory = QueryParameter("anotherMandatory", required = true)

    val scenarios = Seq(
      Scenario("/sa/{utr}", "/sa/{utr}"),
      Scenario("/sa/{utr}?mandatory={mandatory}", "/sa/{utr}", List(mandatory)),
      Scenario("/sa/{utr}", "/sa/{utr}", List(optional)),
      Scenario("/sa/{utr}?mandatory={mandatory}", "/sa/{utr}", List(optional, mandatory)),
      Scenario("/sa/{utr}?mandatory={mandatory}&anotherMandatory={anotherMandatory}", "/sa/{utr}", List(optional, mandatory, anotherMandatory))
    )

    scenarios.foreach(scenario => {
      s"return ${scenario.outputUriPattern} when given ${scenario.inputUriPattern} with parameters: ${scenario.inputParameters}" in {
        anEndpoint(scenario.inputUriPattern, scenario.inputParameters).decoratedUriPattern shouldBe scenario.outputUriPattern
      }
    })

    def anEndpoint(uriPattern: String, parameters: List[QueryParameter]) = {
      Endpoint(uriPattern = uriPattern, endpointName = "Get Today's Date", method = HttpMethod.GET, authType = AuthType.APPLICATION, queryParameters = parameters)
    }
  }

}
