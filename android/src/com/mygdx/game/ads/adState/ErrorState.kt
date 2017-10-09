package com.mygdx.game.ads.adState

import com.mygdx.game.ads.Provider

class ErrorState<T>(val provider: Provider<T>) : ProviderState<T> {

    override fun show() {
         provider.next?.show()
    }

    override fun reset() {
        provider.changeState(NotLoadedState(provider))
    }

}