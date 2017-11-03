package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.ads.EmptyAd;
import com.mygdx.game.analytics.EmptyAnalytics;

import adictive.games.SquareGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SquareGame(new EmptyAd(), new EmptyAnalytics()), config);
	}
}
