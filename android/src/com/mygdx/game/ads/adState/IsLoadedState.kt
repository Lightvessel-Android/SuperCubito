package com.mygdx.game.ads.adState

import com.mygdx.game.ads.Provider
import timber.log.Timber

class IsLoadedState<T> (val provider: Provider<T>, showAfterLoad: Boolean = false) : ProviderState<T> {


    init {
        if(showAfterLoad)
            show()
    }

    override fun show() {
        provider.showAd()
        provider.changeState(NotLoadedState(provider))
    }

    override fun error() {
        Timber.e("error receive but instertitial was not shown")
    }


}