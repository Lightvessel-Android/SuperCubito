package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.ads.AdConfig;
import com.mygdx.game.ads.InterstitialAd;
import com.mygdx.game.analytics.FbAnalytics;

import adictive.games.SquareGame;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AdConfig.INSTANCE.initializeAdNetwork();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new SquareGame(new InterstitialAd(this), new FbAnalytics(this)), config);
	}
}
