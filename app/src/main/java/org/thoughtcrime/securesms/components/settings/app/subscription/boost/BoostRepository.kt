package org.thoughtcrime.securesms.components.settings.app.subscription.boost

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.signal.core.util.money.FiatMoney
import org.thoughtcrime.securesms.badges.Badges
import org.thoughtcrime.securesms.badges.models.Badge
import org.thoughtcrime.securesms.dependencies.ApplicationDependencies
import org.thoughtcrime.securesms.util.PlatformCurrencyUtil
import org.whispersystems.signalservice.api.profiles.SignalServiceProfile
import org.whispersystems.signalservice.api.services.DonationsService
import org.whispersystems.signalservice.internal.ServiceResponse
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

class BoostRepository(private val donationsService: DonationsService) {

  fun getBoosts(): Single<Map<Currency, List<Boost>>> {
    return Single.fromCallable { donationsService.boostAmounts }
      .subscribeOn(Schedulers.io())
      .flatMap(ServiceResponse<Map<String, List<BigDecimal>>>::flattenResult)
      .map { result ->
        result
          .filter { PlatformCurrencyUtil.getAvailableCurrencyCodes().contains(it.key) }
          .mapKeys { (code, _) -> Currency.getInstance(code) }
          .mapValues { (currency, prices) -> prices.map { Boost(FiatMoney(it, currency)) } }
      }
  }

  fun getBoostBadge(): Single<Badge> {
    return Single
      .fromCallable {
        ApplicationDependencies.getDonationsService()
          .getBoostBadge(Locale.getDefault())
      }
      .subscribeOn(Schedulers.io())
      .flatMap(ServiceResponse<SignalServiceProfile.Badge>::flattenResult)
      .map(Badges::fromServiceBadge)
  }
}
