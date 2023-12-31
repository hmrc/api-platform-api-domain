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

class SubscriptionThrottlingTierSpec extends BaseJsonFormattersSpec with TableDrivenPropertyChecks {

  "SubscriptionThrottlingTier" should {
    val values =
      Table(
        ("Tier", "text"),
        (SubscriptionThrottlingTier.BRONZE_SUBSCRIPTION, "bronze"),
        (SubscriptionThrottlingTier.SILVER_SUBSCRIPTION, "silver"),
        (SubscriptionThrottlingTier.GOLD_SUBSCRIPTION, "gold"),
        (SubscriptionThrottlingTier.PLATINUM_SUBSCRIPTION, "platinum"),
        (SubscriptionThrottlingTier.RHODIUM_SUBSCRIPTION, "rhodium")
      )

    "convert to string correctly" in {
      forAll(values) { (s, t) =>
        s.toString() shouldBe t.toUpperCase() + "_SUBSCRIPTION"
      }
    }
    "convert to short string correctly" in {
      forAll(values) { (s, t) =>
        SubscriptionThrottlingTier.description(s) shouldBe t.toUpperCase()
      }
    }
    "convert lower case string to case object" in {
      forAll(values) { (s, t) =>
        SubscriptionThrottlingTier.apply(t) shouldBe Some(s)
        SubscriptionThrottlingTier.unsafeApply(t) shouldBe s
      }
    }

    "convert mixed case string to case object" in {
      forAll(values) { (s, t) =>
        SubscriptionThrottlingTier.apply(t.toUpperCase()) shouldBe Some(s)
        SubscriptionThrottlingTier.unsafeApply(t.toUpperCase()) shouldBe s
      }
    }

    "convert string value to None when undefined or empty" in {
      SubscriptionThrottlingTier.apply("rubbish") shouldBe None
      SubscriptionThrottlingTier.apply("") shouldBe None
    }

    "throw when string value is invalid" in {
      intercept[RuntimeException] {
        SubscriptionThrottlingTier.unsafeApply("rubbish")
      }.getMessage() should include("Subscription Throttling Tier")
    }

    "read from Json" in {
      forAll(values) { (s, t) =>
        testFromJson[SubscriptionThrottlingTier](s""""$t"""")(s)
      }
    }

    "write to Json" in {
      forAll(values) { (s, t) =>
        Json.toJson[SubscriptionThrottlingTier](s) shouldBe JsString(t.toUpperCase())
      }
    }
  }
}
