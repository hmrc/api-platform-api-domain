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

import uk.gov.hmrc.apiplatform.modules.common.domain.services.SimpleEnumJsonFormatting

enum ApiCategory(val displayText: String) {
  case Example                    extends ApiCategory("Example")
  case Agents                     extends ApiCategory("Agents")
  case BusinessRates              extends ApiCategory("Business Rates")
  case Charities                  extends ApiCategory("Charities")
  case ConstructionIndustryScheme extends ApiCategory("Construction Industry Scheme")
  case CorporationTax             extends ApiCategory("Corporation Tax")
  case Customs                    extends ApiCategory("Customs")
  case Estates                    extends ApiCategory("Estates")
  case HelpToSave                 extends ApiCategory("Help to Save")
  case IncomeTaxMtd               extends ApiCategory("Income Tax (Making Tax Digital)")
  case LifetimeIsa                extends ApiCategory("Lifetime ISA")
  case MarriageAllowance          extends ApiCategory("Marriage Allowance")
  case NationalInsurance          extends ApiCategory("National Insurance")
  case Paye                       extends ApiCategory("PAYE")
  case Pensions                   extends ApiCategory("Pensions")
  case PrivateGovernment          extends ApiCategory("Private Government")
  case ReliefAtSource             extends ApiCategory("Relief at Source")
  case SelfAssessment             extends ApiCategory("Self Assessment")
  case StampDuty                  extends ApiCategory("Stamp Duty")
  case Trusts                     extends ApiCategory("Trusts")
  case Vat                        extends ApiCategory("VAT")
  case VatMtd                     extends ApiCategory("VAT (Making Tax Digital)")
  case Other                      extends ApiCategory("Other")
}

object ApiCategory {

  def apply(text: String): Option[ApiCategory] = ApiCategory.values.find(_.toString.equalsIgnoreCase(text))

  def unsafeApply(text: String): ApiCategory = apply(text).getOrElse(throw new RuntimeException(s"$text is not a valid API Category"))

  implicit val ordering: Ordering[ApiCategory] = Ordering.by[ApiCategory, String](_.displayText)

  import play.api.libs.json.Format

  implicit val format: Format[ApiCategory] = SimpleEnumJsonFormatting.createEnumFormatFor[ApiCategory]("API Category", apply)

}
