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

import uk.gov.hmrc.apiplatform.modules.common.domain.models.ApiVersionNbr
import uk.gov.hmrc.apiplatform.modules.common.utils._

class ExtendedApiVersionSpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks {

  "decoratedUriPattern" should {
    case class Scenario(displayedStatus: String, productionAvailability: Option[ApiAvailability], sandboxAvailability: Option[ApiAvailability])

    val privateAccess = Some(ApiAvailability(endpointsEnabled = true, access = ApiAccess.Private(), loggedIn = true, authorised = true))
    val publicAccess  = Some(ApiAvailability(endpointsEnabled = true, access = ApiAccess.PUBLIC, loggedIn = true, authorised = true))

    val scenarios = Seq(
      Scenario("Private Beta", privateAccess, None),
      Scenario("Beta", privateAccess, publicAccess),
      Scenario("Private Beta", privateAccess, privateAccess),
      Scenario("Beta", publicAccess, None),
      Scenario("Beta", publicAccess, publicAccess),
      Scenario("Private Beta", publicAccess, privateAccess),
      Scenario("Beta", None, None),
      Scenario("Beta", None, publicAccess),
      Scenario("Private Beta", None, privateAccess)
    )

    scenarios.foreach(scenario => {
      s"return ${scenario.displayedStatus} when given ${scenario.productionAvailability} and ${scenario.sandboxAvailability}" in {
        anEndpoint(scenario.productionAvailability, scenario.sandboxAvailability).displayedStatus shouldBe scenario.displayedStatus
      }
    })

    def anEndpoint(productionAvailability: Option[ApiAvailability], sandboxAvailability: Option[ApiAvailability]) = {
      ExtendedApiVersion(ApiVersionNbr("1.0"), ApiStatus.BETA, List.empty, productionAvailability, sandboxAvailability)
    }
  }

}
