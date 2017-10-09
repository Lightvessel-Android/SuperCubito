package com.mygdx.game.ads.adState

import timber.log.Timber


interface ProviderState<T>{

    fun show(){
        Timber.e("invalid state: Show")
    }

    fun loaded(ad: T){
        Timber.e("invalid state: Loaded")
    }

    fun error(){
        Timber.e("invalid state: Error")
    }

    fun reset(){
        Timber.e("invalid state: Reset")
    }

    fun load(){
        Timber.e("invalid state: Load")
    }
}