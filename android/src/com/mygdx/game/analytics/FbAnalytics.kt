package com.mygdx.game.analytics
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

class FbAnalytics() : Analytic {
    init {
//        logger = AppEventsLogger(context);
    }
    override fun gameOver() {
//        logger.logEvent("gameOver");
    }

    override fun startLevel() {
//        logger.logEvent("startLevel");
    }

    override fun nextLevel() {
        //logger.logEvent("nextLevel");
    }
}