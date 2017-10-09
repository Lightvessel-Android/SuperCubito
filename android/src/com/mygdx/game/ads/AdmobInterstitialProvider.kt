//package com.mygdx.game.ads
//
//import com.google.android.gms.ads.InterstitialAd
//import com.mygdx.game.ads.AdmobProvider
//import timber.log.Timber
//
//class AdmobInterstitialProvider(next: Provider<*>?) : AdmobProvider<InterstitialAd>(next) {
//
//    init {
//        adObject = InterstitialAd(BaseApplication.Companion.appContext)
//        adObject!!.adUnitId = RemoteConfig.adPlacementAdmobInterstitial()
//    }
//
//    override  fun loadAd(){
//        adObject?.adListener = this
//        adObject?.loadAd(AdContainer.getAdmobAdRequest())
//    }
//
//    override fun showAd(){
//        Timber.d("show interstitial %s %s %s", state.toString(), adObject?.isLoaded.toString(), adObject?.isLoading.toString() )
//        adObject?.show()
//        LogEvent.interstitialShow()
//    }
//}