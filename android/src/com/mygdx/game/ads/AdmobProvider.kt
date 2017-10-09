package com.mygdx.game.ads

import com.google.android.gms.ads.AdListener
import com.mygdx.game.ads.adState.NotLoadedState
import com.mygdx.game.ads.adState.ProviderState
import timber.log.Timber


open class AdmobProvider<T>(override var next: Provider<*>?) : Provider<T>, AdListener() {

    var adObject: T?  = null

    override var state: ProviderState<T> = NotLoadedState(this)


    override  fun loadAd(){}

    override fun showAd(){}

    override fun onAdClosed() {
        // do nothing
    }

    override fun onAdLoaded() {
        Timber.d("Ad Loaded: %s", adObject)
        state.loaded(adObject as T)
    }

    override fun onAdFailedToLoad(errorCode: Int) {
        Timber.d("Couldn't download ad: CODE %d %s", errorCode, this.toString())
        state.error()
    }

    override fun onAdOpened() {
        super.onAdOpened()
    }
}