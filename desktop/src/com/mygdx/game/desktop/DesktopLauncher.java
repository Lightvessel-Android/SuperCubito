package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.ads.EmptyAd;
import com.mygdx.game.analytics.EmptyAnalytics;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new SuperCubito(new EmptyAd(), new EmptyAnalytics()), config);
	}
}
