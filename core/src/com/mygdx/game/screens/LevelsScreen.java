package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.ads.AdInterface;
import com.mygdx.game.analytics.Analytic;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.Settings;

import static com.mygdx.game.utils.Settings.levelMax;

public class LevelsScreen extends ScreenAdapter {

    private SuperCubito game;

    private Stage stage;

    private Table main;

    private ScrollPane scroll;
    private AdInterface adInterface;
    private Analytic analytic;

    private OrthographicCamera guiCam;

    public LevelsScreen(final SuperCubito gameE, AdInterface adInterface, Analytic analytic) {
        game = gameE;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        stage = new Stage(new ScreenViewport());

        main = new Table(Assets.skin);
        generateLevels();
        main.setHeight(stage.getHeight());
        main.setWidth(stage.getWidth());

        main.setBackground(new TextureRegionDrawable(Assets.background));

        scroll = new ScrollPane(main);
        scroll.setFillParent(true);
        scroll.setOverscroll(false, false);

        stage.addActor(scroll);

        this.adInterface = adInterface;
        this.analytic = analytic;
    }

    private void generateLevels() {
        for (int i = 0; i < Assets.levels.size(); i++) {
            final int level = i;

            final Button button = new TextButton("" + (level + 1), Assets.skin);

            if(level > levelMax){
                button.setStyle(Assets.lockedStyle);
            } else button.setStyle(Assets.unLockedStyle);


            button.addCaptureListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(levelMax >= level) {
                        Settings.actualLevel = level;
                        analytic.startLevel();
                        game.setScreen(new GameScreen(game, Assets.getLevel(level), adInterface, analytic));
                    }
                }
            });

            main.add(button).size(100, 75).spaceBottom(20).spaceRight(20);

            int countMax = (int) (stage.getWidth() / 120);

            if((i+1) % countMax == 0)
                main.row();
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
