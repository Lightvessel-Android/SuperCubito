package com.mygdx.game.ads

import com.facebook.ads.AdSettings
import com.google.android.gms.ads.AdRequest
import com.mygdx.game.BuildConfig

object AdConfig {

    fun getAdmobAdRequest(): AdRequest {
        val builder = AdRequest.Builder()
        if (BuildConfig.DEBUG){
            AdRequest.DEVICE_ID_EMULATOR
        }
        return builder.build()
    }

    fun initializeAdNetwork() {
        if (BuildConfig.DEBUG) {
            AdSettings.addTestDevices(listOf(
                    "c1a4343588297d20b882344cdfaa2bd2"
                ))
        }
    }

    fun adPlacementAdmobInterstitial() : String {
        return "sarasa"
    }

    fun adPlacementFacebookInterstitial() : String {
        return "156496048280442_156758051587575"
    }
}

