package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.utils.Assets;

import java.util.ArrayList;
import java.util.List;

public class LevelsScreen extends ScreenAdapter {

    private Skin skin;

    private SuperCubito game;

    private OrthographicCamera guiCam;

    private Vector3 touchPoint;

    private List<Rectangle> buttons;

    public LevelsScreen(final SuperCubito gameE) {
        skin = Assets.skin;
        game = gameE;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        touchPoint = new Vector3();

        buttons = new ArrayList<Rectangle>();

        generateLevels();
    }

    private void generateLevels() {

        for (int i = 0; i < Assets.levels.size(); i++) {

            Rectangle rec = new Rectangle(10, 420, 50, 50);


            buttons.add(rec);


//            TextButton button = new TextButton("Level " + i, skin);
//
//            final int finalI = i;
//
//            button.addCaptureListener(new ChangeListener() {
//                @Override
//                public void changed(ChangeEvent event, Actor actor) {
//                    game.setScreen(new GameScreen(game, Assets.levels.get(finalI)));
//                }
//            });
//
//            buttons.add(button);
        }
    }


    @Override
    public void render(float delta) {
        update();
        draw();
    }

    public void update() {

        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (Rectangle rectangle : buttons) {
                if (rectangle.contains(touchPoint.x, touchPoint.y)) {
                    game.setScreen(new GameScreen(game, Assets.levels.get(buttons.indexOf(rectangle))));
                    return;
                }
            }
        }
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0.4f, 0.5f,0.8f,1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        game.batcher.setProjectionMatrix(guiCam.combined);

        game.batcher.disableBlending();
        game.batcher.begin();
        game.batcher.end();

        game.batcher.enableBlending();
        game.batcher.begin();

        for (int i = 0; i < buttons.size(); i++) {
            Rectangle rectangle = buttons.get(i);
            game.batcher.draw(Assets.button, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            Assets.font.draw(game.batcher, "Level " +  (i + 1), rectangle.getX() + rectangle.getWidth()/4, rectangle.getY() + rectangle.getHeight()/2);
        }

        game.batcher.end();
    }

}
