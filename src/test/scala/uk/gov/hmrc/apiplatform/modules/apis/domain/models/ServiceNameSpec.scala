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

import play.api.libs.json._

import uk.gov.hmrc.apiplatform.modules.common.utils.BaseJsonFormattersSpec

class ServiceNameSpec extends BaseJsonFormattersSpec {

  "ServiceName" should {
    val example = ServiceName("miscblah")

    "convert toString" in {
      example.toString() shouldBe "miscblah"
    }

    "read from Json" in {
      testFromJson[ServiceName](s""""miscblah"""")(example)
    }

    "write to Json" in {
      Json.toJson[ServiceName](example) shouldBe JsString("miscblah")
    }

    "order correctly" in {
      val names = List("a", "b", "c", "d", "e").map(ServiceName(_))

      Random.shuffle(names).sorted shouldBe names
    }
  }
}
