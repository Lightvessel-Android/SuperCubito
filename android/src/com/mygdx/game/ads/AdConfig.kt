package com.mygdx.game.ads

import com.google.android.gms.ads.AdRequest
import com.mygdx.game.BuildConfig

object AdConfig {

    fun getAdmobAdRequest(): AdRequest {
        val builder = AdRequest.Builder()
        if (BuildConfig.DEBUG){
//            builder.addTestDevice(BuildConfig.ADMOB_TEST_DEVICE_KEY)
            builder.addTestDevice("7C1BCD1372AE7D2209ABDA89389B5356")
//            builder.addTestDevice(Settings.Secure.getString(BaseApplication.appContext.contentResolver, Settings.Secure.ANDROID_ID))
        }
        return builder.build()
    }

    fun adPlacementAdmobInterstitial() : String {
        return "sarasa"
    }
}

