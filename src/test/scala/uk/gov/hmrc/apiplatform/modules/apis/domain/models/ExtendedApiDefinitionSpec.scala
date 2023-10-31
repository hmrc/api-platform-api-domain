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

import scala.util.Random

import org.scalatest.prop.TableDrivenPropertyChecks

import uk.gov.hmrc.apiplatform.modules.apis.domain.models._
import uk.gov.hmrc.apiplatform.modules.common.utils._

class ExtendedApiDefinitionSpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks with ApiDefinitionFactory {

  "ExtendedApiDefinition.defaultVersion" should {

    val v0_9Published      = buildExtendedVersion("0.9", ApiStatus.STABLE)
    val v1Retired          = buildExtendedVersion("1.0", ApiStatus.RETIRED)
    val v2Published        = buildExtendedVersion("2.0", ApiStatus.STABLE)
    val v2Deprecated       = buildExtendedVersion("2.0", ApiStatus.DEPRECATED)
    val v3Prototyped       = buildExtendedVersion("3.0", ApiStatus.BETA)
    val v3Published        = buildExtendedVersion("3.0", ApiStatus.STABLE)
    val v3PrivatePublished = buildExtendedVersion("3.1", ApiStatus.BETA)
    val v4Alpha            = buildExtendedVersion("4.0", ApiStatus.ALPHA)
    val v10Published       = buildExtendedVersion("10.0", ApiStatus.STABLE)

    val defaultVersionsScenarios = Table(
      ("Versions", "Expected Default Version"),
      (List(v3Prototyped, v2Published), v2Published),
      (List(v2Deprecated, v3Published), v3Published),
      (List(v2Deprecated, v1Retired), v2Deprecated),
      (List(v3Prototyped, v2Published, v1Retired), v2Published),
      (List(v2Published, v3Published, v0_9Published), v3Published),
      (List(v3Prototyped, v2Published, v10Published, v1Retired), v10Published),
      (List(v3Prototyped, v3PrivatePublished), v3PrivatePublished),
      (List(v3PrivatePublished, v3Prototyped), v3PrivatePublished),
      (List(v4Alpha, v3Prototyped), v3Prototyped),
      (List(v4Alpha, v3Prototyped, v2Published), v2Published)
    )

    "return the default version depending on the status and version" in {

      forAll(defaultVersionsScenarios) { (versions, expectedDefaultVersion) =>
        val api = buildExtendedDefinition(Random.shuffle(versions))

        api.defaultVersion.value shouldEqual expectedDefaultVersion
      }
    }

    val sortedVersionsScenarios = Table(
      ("Versions", "Expected Sorts Version"),
      (List(v3Prototyped, v2Published), List(v3Prototyped, v2Published)),
      (List(v2Deprecated, v3Published), List(v3Published, v2Deprecated)),
      (List(v2Deprecated, v1Retired), List(v2Deprecated)),
      (List(v3Prototyped, v2Published, v1Retired), List(v3Prototyped, v2Published)),
      (List(v2Published, v3Published, v0_9Published), List(v3Published, v2Published, v0_9Published)),
      (List(v3Prototyped, v2Published, v10Published, v1Retired), List(v10Published, v3Prototyped, v2Published)),
      (List(v3Prototyped, v3PrivatePublished), List(v3PrivatePublished, v3Prototyped)),
      (List(v3PrivatePublished, v3Prototyped), List(v3PrivatePublished, v3Prototyped)),
      (List(v4Alpha, v3Prototyped), List(v4Alpha, v3Prototyped)),
      (List(v4Alpha, v3Prototyped, v2Published), List(v4Alpha, v3Prototyped, v2Published))
    )

    "return the sorted active version depending on just the version" in {

      forAll(sortedVersionsScenarios) { (versions, expectedSortedVersions) =>
        val api = buildExtendedDefinition(Random.shuffle(versions))

        api.sortedActiveVersions shouldEqual expectedSortedVersions
      }
    }

    val public               = Some(ApiAvailability(true, ApiAccess.PUBLIC, true, true))
    val privateAuthorised    = Some(ApiAvailability(true, ApiAccess.Private(), true, true))
    val privateNotAuthorised = Some(ApiAvailability(true, ApiAccess.Private(), true, false))
    val trialAuthorised      = Some(ApiAvailability(true, ApiAccess.Private(true), true, true))
    val trialNotAuthorised   = Some(ApiAvailability(true, ApiAccess.Private(true), true, false))

    val accessibleVersionsScenarios = Table(
      ("Availabilities", "Is version accessible"),
      ((None, None), false),
      ((public, None), true),
      ((None, public), true),
      ((public, public), true),
      ((privateAuthorised, None), true),
      ((None, privateAuthorised), true),
      ((privateAuthorised, privateAuthorised), true),
      ((privateNotAuthorised, privateAuthorised), true),
      ((None, privateNotAuthorised), false),
      ((privateNotAuthorised, privateNotAuthorised), false),
      ((trialAuthorised, privateNotAuthorised), true),
      ((None, trialAuthorised), true),
      ((trialAuthorised, trialAuthorised), true),
      ((trialNotAuthorised, None), true),
      ((None, trialNotAuthorised), true),
      ((trialNotAuthorised, trialNotAuthorised), true)
    )

    "return the accessible active version depending on just the version" in {

      forAll(accessibleVersionsScenarios) { (apiAvailability, accessible) =>
        val api = buildExtendedDefinition(List(buildExtendedVersion("1.0", productionAvailability = apiAvailability._1, sandboxAvailability = apiAvailability._2)))

        api.userAccessibleApiDefinition.versions.nonEmpty shouldBe accessible
      }
    }
  }

}
