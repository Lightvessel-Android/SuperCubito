//package com.mygdx.game.ads
//
//import android.content.Context
//import android.provider.Settings
//import com.facebook.ads.AdSettings
//import com.google.android.gms.ads.AdRequest
//import com.mygdx.game.ads.AdmobInterstitialProvider
//import com.mobilearb.inspirational.ads.FbInterstitialProvider
//import com.mygdx.game.ads.Provider
//
//
//object AdContainer {
//    var mainContext: Context? = null
//    var firstInterstitialProvider: Provider<*>? = null
//    var firstNativeProvider : Provider<*>? = null
//    var homeNativeProvider :  Provider<*>? = null
//
//    fun initialize(){
//        initializeHomeNative()
//        initializeInterstitial()
//        initializeNative()
//    }
//
//    fun initializeHomeNative(){
//        homeNativeProvider = FbNativeProvider(BaseApplication.appContext, null, RemoteConfig.adPlacementFacebookHomeNative())
//
//        homeNativeProvider?.load()
//    }
//
//    fun initializeNative() {
//        firstNativeProvider = FbNativeProvider(BaseApplication.appContext, null, RemoteConfig.adPlacementFacebookNative())
//
//        firstNativeProvider?.load()
//    }
//
//    fun initializeInterstitial() {
//        firstInterstitialProvider = if(RemoteConfig.adPriorityFacebook() < RemoteConfig.adPriorityAdmob()){
//            FbInterstitialProvider(BaseApplication.appContext, AdmobInterstitialProvider(null))
//        }else {
//            AdmobInterstitialProvider(FbInterstitialProvider(BaseApplication.appContext,null))
//        }
//
//        firstInterstitialProvider?.load()
//    }
//
//    fun getAdmobAdRequest(): AdRequest {
//        val builder = AdRequest.Builder()
//        if (BuildConfig.DEBUG_MODE){
////            builder.addTestDevice(BuildConfig.ADMOB_TEST_DEVICE_KEY)
//            builder.addTestDevice("7C1BCD1372AE7D2209ABDA89389B5356")
////            builder.addTestDevice(Settings.Secure.getString(BaseApplication.appContext.contentResolver, Settings.Secure.ANDROID_ID))
//        }
//        return builder.build()
//    }
//
//    fun loadInterstitial(){
//        firstInterstitialProvider?.load()
//    }
//
//    fun showInterstitial(){
//        firstInterstitialProvider?.show()
//    }
//
//    fun loadNative(){
//        firstNativeProvider?.load()
//    }
//
//    fun showNative(){
//        firstNativeProvider?.show()
//    }
//
//    fun loadHomeNative(){
//        homeNativeProvider?.load()
//    }
//
//    fun showHomeNative(){
//        homeNativeProvider?.show()
//    }
//
//    fun initializeAdNetwork(context: Context) {
//        if (BuildConfig.DEBUG_MODE) {
//            AdSettings.addTestDevices(listOf(
//                    BuildConfig.TEST_DEVICE_KEY,
//                    "9ffa865588bd6b3c67ed1ee99101cb59",
//                    "758b96447a603e21",
//                    "e1936f324b1d13cceb40b8d76c3db310"))
//        }
//
//        mainContext = context
//    }
//}
