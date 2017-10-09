package com.mygdx.game.ads

import com.mygdx.game.ads.adState.ProviderState
import timber.log.Timber

interface Provider<T> {
    var state: ProviderState<T>

    val next: Provider<*>?

    fun load(){
        this.state.load()
    }

    fun loadAd()

    fun show(){
        this.state.show()
    }

    fun changeState(state: ProviderState<T>) {
        Timber.d("%s  -> %s",this.state.javaClass.toString(), state.javaClass.toString())
        this.state = state
    }

    fun showAd()


}