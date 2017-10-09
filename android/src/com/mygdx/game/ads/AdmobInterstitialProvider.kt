package com.mygdx.game.ads

import com.google.android.gms.ads.InterstitialAd
import com.mygdx.game.AndroidLauncher
import timber.log.Timber

class AdmobInterstitialProvider(context : AndroidLauncher, next: Provider<*>?) : AdmobProvider<InterstitialAd>(next) {

    init {
        adObject = InterstitialAd(context)
        adObject!!.adUnitId = AdConfig.adPlacementAdmobInterstitial()
    }

    override  fun loadAd(){
        adObject?.adListener = this
        adObject?.loadAd(AdConfig.getAdmobAdRequest())
    }

    override fun showAd(){
        Timber.d("show interstitial %s %s %s", state.toString(), adObject?.isLoaded.toString(), adObject?.isLoading.toString() )
        adObject?.show()
    }
}