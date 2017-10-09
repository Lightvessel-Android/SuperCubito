package com.mygdx.game.ads

import android.content.Context
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdListener
import com.mygdx.game.ads.adState.NotLoadedState
import com.mygdx.game.ads.adState.ProviderState

open class FbProvider<T>(context: Context, override val  next: Provider<*>?) : Provider<T>, AdListener {

    var adObject: T?  = null

    override var state: ProviderState<T> = NotLoadedState(this)

    override fun loadAd() {}

    override fun showAd() {}


    override fun onLoggingImpression(p0: Ad?) {
    }

    override fun onAdLoaded(ad: Ad?) {
        Log.d("FACEBOOK_ADS", "Ad Loaded: $ad")
        state.loaded(ad as T)
    }

    override fun onAdClicked(ad: Ad?) {
    }

    override fun onError(ad: Ad?, err: AdError) {
        Log.d("FACEBOOK AD ERROR", "Couldn't download ad: CODE ${err.errorCode} MSG ${err.errorMessage}")
        state.error()
    }

}
