package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Settings.levelMax;

public class LevelsScreen extends ScreenAdapter {

    private SuperCubito game;

    private Stage stage;

    private Table main;

    public static int actualLevel = 0;

    private OrthographicCamera guiCam;

    public LevelsScreen(final SuperCubito gameE) {
        game = gameE;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        stage = new Stage(new ScreenViewport());


        main = new Table(Assets.skin);
        main.setFillParent(true);

        main.setBackground(new TextureRegionDrawable(Assets.background));
        main.row();
        main.pad(10F, 10F, 10F, 0F);
        main.top().left();
        main.row();


        generateLevels();
        stage.addActor(main);
    }

    private void generateLevels() {
        for (int i = 0; i < Assets.levels.size(); i++) {
            final int level = i;
            Button button = new TextButton("Level " +  (level + 1), Assets.skin);

            button.addCaptureListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(levelMax >= level) {
                        game.setScreen(new GameScreen(game, Assets.levels.get(level)));
                        actualLevel = level;
                    }
                }
            });
            main.add(button).size(100, 75).spaceBottom(20).spaceRight(20);

            if (main.getCell(button).isEndRow()) {
               main.row();
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.4f, 0.5f,0.8f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

}