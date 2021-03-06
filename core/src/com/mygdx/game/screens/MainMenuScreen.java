package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.Settings;

public class MainMenuScreen extends ScreenAdapter {

    private SuperCubito game;
    private OrthographicCamera guiCam;
    private Vector3 touchPoint;

    private Rectangle playBounds;
    private Rectangle soundBounds;

    public MainMenuScreen(SuperCubito gameC) {
        game = gameC;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        playBounds = new Rectangle(60, 50, 200, 200);
        soundBounds = new Rectangle(0, 0, 64, 64);
        touchPoint = new Vector3();
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    @Override
    public void pause () {
        Settings.save();
    }

    private void update() {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            checkPlayButton();

            checkSoundButton();
        }
    }

    private void checkSoundButton() {
        if (soundBounds.contains(touchPoint.x, touchPoint.y)) {
            Assets.playSound(Assets.clickSound);
            Settings.soundEnabled = !Settings.soundEnabled;
            if (Settings.soundEnabled)
                Assets.music.play();
            else
                Assets.music.pause();
        }
    }

    private void checkPlayButton() {
        if (playBounds.contains(touchPoint.x, touchPoint.y)) {
            Assets.playSound(Assets.clickSound);
            game.setScreen(new LevelsScreen(game));
        }
    }

    private void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        game.batcher.setProjectionMatrix(guiCam.combined);

        game.batcher.begin();
        game.batcher.draw(Assets.mainMenu, 0, 0, 320, 480);
        game.batcher.draw(Assets.playGame, 60, 50, 200, 200);
        game.batcher.draw(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 0, 0, 64, 64);
        game.batcher.end();
    }
}