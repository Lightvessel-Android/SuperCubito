package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ads.AdInterface;
import com.mygdx.game.analytics.Analytic;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.utils.Settings;

public class SuperCubito extends Game {

	public SpriteBatch batcher;
	public final AdInterface adInterface;
	public final Analytic analytic;

	public SuperCubito(AdInterface adInterface, Analytic analytic){
		this.adInterface = adInterface;
		this.analytic = analytic;
	}

	@Override
	public void create () {
		batcher = new SpriteBatch();
		Settings.load();
		com.mygdx.game.utils.Assets.load();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		GL20 gl = Gdx.gl;
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}
