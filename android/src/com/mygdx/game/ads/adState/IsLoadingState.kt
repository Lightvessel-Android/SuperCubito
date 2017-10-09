package com.mygdx.game.ads.adState

import com.mygdx.game.ads.Provider


class IsLoadingState<T>(val provider: Provider<T>, var showAfterLoad: Boolean = false) : ProviderState<T> {

    override fun show() {
        showAfterLoad = true
    }

    override fun loaded(ad: T) {
        provider.changeState(IsLoadedState(provider,showAfterLoad))

    }

    override fun error() {
        provider.changeState(ErrorState(provider))
    }

}