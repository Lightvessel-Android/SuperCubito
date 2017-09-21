package com.mygdx.game.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.systems.EnemySystem;
import com.mygdx.game.systems.InputSystem;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.World;
import com.mygdx.game.states.GameState;
import com.mygdx.game.systems.AnimationSystem;
import com.mygdx.game.systems.BackgroundSystem;
import com.mygdx.game.systems.BoundsSystem;
import com.mygdx.game.systems.CameraSystem;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.StateSystem;

import static com.mygdx.game.states.GameState.GAME_OVER;
import static com.mygdx.game.states.GameState.GAME_PAUSED;
import static com.mygdx.game.states.GameState.GAME_READY;
import static com.mygdx.game.states.GameState.GAME_RUNNING;
import static com.mygdx.game.states.WorldState.WORLD_STATE_GAME_OVER;
import static com.mygdx.game.states.WorldState.WORLD_STATE_NEXT_LEVEL;

public class GameScreen extends ScreenAdapter {

    SuperCubito game;

    OrthographicCamera guiCam;
    Vector3 touchPoint;
    World world;
    CollisionSystem.CollisionListener collisionListener;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    PooledEngine engine;
    private GlyphLayout layout = new GlyphLayout();

    private GameState state;

    public GameScreen (SuperCubito game) {
        this.game = game;

        state = GAME_RUNNING;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        touchPoint = new Vector3();
        collisionListener = new CollisionSystem.CollisionListener() {
            @Override
            public void coin () {
                Assets.playSound(Assets.coinSound);
            }

            @Override
            public void dead () {
                Assets.playSound(Assets.deadSound);
            }
        };

        engine = new PooledEngine();

        world = new World(engine);

        engine.addSystem(new CameraSystem());
        engine.addSystem(new BackgroundSystem());
        engine.addSystem(new InputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new CollisionSystem(world, collisionListener));
        engine.addSystem(new PlayerSystem(world));
        engine.addSystem(new RenderingSystem(game.batcher));

        engine.addSystem(new StateSystem());
        engine.addSystem(new AnimationSystem());

        engine.addSystem(new EnemySystem());

        engine.getSystem(BackgroundSystem.class).setCamera(engine.getSystem(RenderingSystem.class).getCamera());

        world.create();

        pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);

        resumeBounds = new Rectangle(160 - 96, 36, 192, 36);

        quitBounds = new Rectangle(160 - 96,0, 192, 36);

//        pauseSystems();
    }

    public void update (float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        engine.update(deltaTime);

        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_LEVEL_END:
                updateLevelEnd();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updateReady () {
        if (Gdx.input.justTouched()) {
            state = GAME_RUNNING;
            resumeSystems();
        }
    }

    private void updateRunning (float deltaTime) {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_PAUSED;
                pauseSystems();
                return;
            }
        }

        Application.ApplicationType appType = Gdx.app.getType();

        float accelX = 0.0f;

        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            accelX = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) accelX = 5f;
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) accelX = -5f;
        }

        engine.getSystem(PlayerSystem.class).setAccelX(accelX);

        if (world.state == WORLD_STATE_NEXT_LEVEL) {
            game.setScreen(new MainMenuScreen(game));
        }

        if (world.state == WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            pauseSystems();
        }
    }

    private void updatePaused () {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                resumeSystems();
                return;
            }

            if (quitBounds.contains(touchPoint.x, touchPoint.y)) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

    private void updateLevelEnd () {
        if (Gdx.input.justTouched()) {
            engine.removeAllEntities();
            // ACA pasar al sig lvl
            world = new World(engine);
            state = GAME_READY;
        }
    }

    private void updateGameOver () {
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    public void drawUI () {
        guiCam.update();
        game.batcher.setProjectionMatrix(guiCam.combined);
        game.batcher.begin();
        switch (state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_PAUSED:
                presentPaused();
                break;
            case GAME_LEVEL_END:
                presentLevelEnd();
                break;
            case GAME_OVER:
                presentGameOver();
                break;
        }
        game.batcher.end();
    }

    private void presentReady () {
        game.batcher.draw(Assets.ready, 160 - 192 / 2, 440 - 32 / 2, 192, 32);
    }

    private void presentRunning () {
        game.batcher.draw(Assets.pause, 320 - 64, 480 - 64, 64, 64);
    }

    private void presentPaused () {
        game.batcher.draw(Assets.pauseMenu, 160 - 192 / 2, 0 / 2, 192, 96);
    }

    private void presentLevelEnd () {
        String topText = "the princess is ...";
        String bottomText = "in another castle!";

        layout.setText(Assets.font, topText);
        float topWidth = layout.width;

        layout.setText(Assets.font, bottomText);
        float bottomWidth = layout.width;
        Assets.font.draw(game.batcher, topText, 160 - topWidth / 2, 480 - 40);
        Assets.font.draw(game.batcher, bottomText, 160 - bottomWidth / 2, 40);
    }

    private void presentGameOver () {
        game.batcher.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
    }

    private void pauseSystems() {
        engine.getSystem(PlayerSystem.class).setProcessing(false);
        engine.getSystem(CollisionSystem.class).setProcessing(false);
        engine.getSystem(MovementSystem.class).setProcessing(false);
        engine.getSystem(BoundsSystem.class).setProcessing(false);
        engine.getSystem(StateSystem.class).setProcessing(false);
        engine.getSystem(AnimationSystem.class).setProcessing(false);
        engine.getSystem(EnemySystem.class).setProcessing(false);
        engine.getSystem(InputSystem.class).setProcessing(false);

    }

    private void resumeSystems() {
        engine.getSystem(PlayerSystem.class).setProcessing(true);
        engine.getSystem(CollisionSystem.class).setProcessing(true);
        engine.getSystem(MovementSystem.class).setProcessing(true);
        engine.getSystem(BoundsSystem.class).setProcessing(true);
        engine.getSystem(StateSystem.class).setProcessing(true);
        engine.getSystem(AnimationSystem.class).setProcessing(true);
        engine.getSystem(EnemySystem.class).setProcessing(true);
        engine.getSystem(InputSystem.class).setProcessing(true);
    }

    @Override
    public void render (float delta) {
        update(delta);
        drawUI();
    }

    @Override
    public void pause () {
        if (state == GAME_RUNNING) {
            state = GAME_PAUSED;
            pauseSystems();
        }
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
