package com.mygdx.game.ads.adState

import com.mygdx.game.ads.Provider


class NotLoadedState<T>(val provider: Provider<T>) : ProviderState<T> {

    override fun show() {
        provider.changeState(IsLoadingState(provider,showAfterLoad=true))
        provider.loadAd()
    }

    override fun reset() {
        provider.load()
    }

    override fun load() {
        provider.changeState(IsLoadingState(provider,showAfterLoad=false))
        provider.loadAd()
    }

}