package com.mygdx.game.analytics
import android.content.Context
import com.facebook.appevents.AppEventsLogger;

class FbAnalytics(context : Context) : Analytic {

    private val logger : AppEventsLogger = AppEventsLogger.newLogger(context)

    override fun gameOver(level : Int) {
        logger.logEvent("gameOver level: " + level, 1.0)
    }

    override fun startLevel(level : Int) {
        logger.logEvent("startLevel: " + level, 1.0)
    }

    override fun nextLevel(level : Int) {
        logger.logEvent("nextLevel: " + level, 1.0)
    }
}