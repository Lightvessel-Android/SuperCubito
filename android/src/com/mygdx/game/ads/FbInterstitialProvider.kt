package com.mygdx.game.ads

import android.content.Context
import com.facebook.ads.Ad
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.mygdx.game.ads.adState.NotLoadedState

class FbInterstitialProvider(context: Context, next: Provider<*>?) : FbProvider<InterstitialAd>(context,next), InterstitialAdListener {

    init {
        adObject = InterstitialAd(context, AdConfig.adPlacementFacebookInterstitial())
        adObject!!.setAdListener(this)
    }

    override fun loadAd(){
        adObject?.loadAd()
    }

    override fun showAd(){
        adObject?.show()
    }


    override fun onInterstitialDismissed(ad: Ad?) {
        state.reset()
    }

    override fun onInterstitialDisplayed(ad: Ad?) {
        changeState(NotLoadedState(this))
    }
}