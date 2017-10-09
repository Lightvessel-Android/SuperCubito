//package com.mobilearb.inspirational.ads
//
//import android.content.Context
//import com.facebook.ads.Ad
//import com.facebook.ads.InterstitialAd
//import com.facebook.ads.InterstitialAdListener
//import com.mobilearb.inspirational.config.RemoteConfig
//import com.mobilearb.inspirational.events.LogEvent
//import com.mygdx.game.ads.Provider
//import com.mygdx.game.ads.adState.NotLoadedState
//
//class FbInterstitialProvider(context: Context, next: Provider<*>?) : FbProvider<InterstitialAd>(context,next), InterstitialAdListener {
//
//    init {
//        adObject = InterstitialAd(context, RemoteConfig.adPlacementFacebookInterstitial())
//        adObject!!.setAdListener(this)
//    }
//
//    override fun loadAd(){
//        adObject?.loadAd()
//    }
//
//    override fun showAd(){
//        adObject?.show()
//    }
//
//
//    override fun onInterstitialDismissed(ad: Ad?) {
//        state.reset()
//    }
//
//    override fun onInterstitialDisplayed(ad: Ad?) {
//        LogEvent.interstitialShow()
//        changeState(NotLoadedState(this))
//    }
//}