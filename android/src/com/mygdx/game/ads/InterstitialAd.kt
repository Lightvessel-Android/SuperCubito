package com.mygdx.game.ads;

import com.mygdx.game.AndroidLauncher

class InterstitialAd(context : AndroidLauncher) : AdInterface {

    var firstInterstitialProvider: Provider<*>? = null

    init {
        firstInterstitialProvider = FbInterstitialProvider(context, null) //AdmobInterstitialProvider(context, null)
        firstInterstitialProvider?.load()
    }


    override fun showAd() {
        firstInterstitialProvider?.show()
    }


}
