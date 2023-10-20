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

import uk.gov.hmrc.apiplatform.modules.common.utils.HmrcSpec

class ApiVersionsSpec extends HmrcSpec with ApiDefinitionFactory {
 
  val version1 = buildVersion("1.0")
  val version2 = buildVersion("2.0")
  val version3 = buildVersion("3.0")

  "ApiVersions" should {
    "convert from empty list" in {
      ApiVersions.fromList(List.empty) shouldBe Map.empty
    }

    "convert from list" in {
      val versions = List(
        buildVersion("1.0"),
        buildVersion("2.0"),
        buildVersion("3.0")
      )

      ApiVersions.fromList(versions).keySet should contain.allOf (version1.versionNbr, version2.versionNbr, version3.versionNbr)
    }

    "convert from list removing duplicate version numbers" in {
      val versions = List(
        buildVersion("1.0"),
        buildVersion("1.0"),
        buildVersion("2.0"),
        buildVersion("2.0"),
        buildVersion("3.0")
      )

      val result = ApiVersions.fromList(versions).keySet
      
      result should contain.allOf (version1.versionNbr, version2.versionNbr, version3.versionNbr)
      result.size shouldBe 3
    }
  }
}
