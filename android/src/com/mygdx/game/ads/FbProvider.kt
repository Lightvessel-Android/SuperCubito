//package com.mobilearb.inspirational.ads
//
//import android.content.Context
//import android.util.Log
//import com.facebook.ads.*
//import com.mobilearb.inspirational.config.RemoteConfig
//import com.mobilearb.inspirational.events.LogEvent
//import com.mygdx.game.ads.Provider
//import com.mygdx.game.ads.adState.NotLoadedState
//import com.mygdx.game.ads.adState.ProviderState
//
//open class FbProvider<T>(context: Context, override val  next: Provider<*>?) : Provider<T>, AdListener {
//
//    var adObject: T?  = null
//
//    override var state: ProviderState<T> = NotLoadedState(this)
//
//    override fun loadAd() {}
//
//    override fun showAd() {}
//
//
//    override fun onLoggingImpression(p0: Ad?) {
//        LogEvent.fakePurchase()
//    }
//
//    override fun onAdLoaded(ad: Ad?) {
//        Log.d("FACEBOOK_ADS", "Ad Loaded: $ad")
//        state.loaded(ad as T)
//    }
//
//    override fun onAdClicked(ad: Ad?) {
//        LogEvent.fbAdClicked()
//    }
//
//    override fun onError(ad: Ad?, err: AdError) {
//        Log.d("FACEBOOK AD ERROR", "Couldn't download ad: CODE ${err.errorCode} MSG ${err.errorMessage}")
//        state.error()
//    }
//
//}
