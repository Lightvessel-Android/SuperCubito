package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.utils.Assets;

import java.util.ArrayList;
import java.util.List;

import static com.mygdx.game.utils.Settings.levelMax;

public class LevelsScreen extends ScreenAdapter {

    private SuperCubito game;

    public static int actualLevel = 1;

    private OrthographicCamera guiCam;

    private Vector3 touchPoint;

    private List<Rectangle> buttons;

    public LevelsScreen(final SuperCubito gameE) {
        game = gameE;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        touchPoint = new Vector3();

        buttons = new ArrayList<Rectangle>();

        generateLevels();
    }

    private void generateLevels() {

        float max_x = guiCam.viewportWidth;

        for (int i = 0; i < Assets.levels.size(); i++) {
            Rectangle rec = new Rectangle();
            rec.setHeight(50);
            rec.setWidth(50);

            if(i == 0) {
                rec.setPosition(10, 420);
            } else {
                Rectangle succ = buttons.get(i - 1);
                if(succ.getX() + succ.getWidth() * 2 + 10 > max_x){
                    rec.setPosition(10, succ.getY() - succ.getHeight() - 10);
                } else {
                    rec.setPosition(succ.getX() + succ.getWidth() + 10, succ.getY());
                }
            }

            buttons.add(rec);
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
                int index = buttons.indexOf(rectangle);
                if (rectangle.contains(touchPoint.x, touchPoint.y) && levelMax >= index + 1) {
                    game.setScreen(new GameScreen(game, Assets.levels.get(index)));
                    actualLevel = index + 1;
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
