package com.mygdx.game.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import adictive.games.analytics.IAnalyticsManager;

public class FirebaseAnalyticsManager implements IAnalyticsManager {
    private static final String LEVEL_LOST = "level_lost";

    private FirebaseAnalytics analytics;
    private Context context;

    public FirebaseAnalyticsManager(Context context) {
        this.context = context;
    }

    private FirebaseAnalytics getAnalytics() {
        if (analytics == null) {
            analytics = FirebaseAnalytics.getInstance(context);
        }
        return analytics;
    }

    @Override
    public void winLevel(int level) {
        Bundle params = new Bundle();
        params.putLong(FirebaseAnalytics.Param.LEVEL, level);
        getAnalytics().logEvent(FirebaseAnalytics.Event.LEVEL_UP, params);
    }

    @Override
    public void loseLevel(int level) {
        Bundle params = new Bundle();
        params.putLong(FirebaseAnalytics.Param.LEVEL, level);
        getAnalytics().logEvent(LEVEL_LOST, params);
    }
}
