package com.mygdx.game.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.SuperCubito;
import com.mygdx.game.World;
import com.mygdx.game.enums.GameState;
import com.mygdx.game.systems.BackgroundSystem;
import com.mygdx.game.systems.BoundsSystem;
import com.mygdx.game.systems.CameraSystem;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.DebugSystem;
import com.mygdx.game.systems.EnemySystem;
import com.mygdx.game.systems.InputSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.TransformerEntitiesSystem;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.Settings;

import static com.mygdx.game.enums.GameState.GAME_OVER;
import static com.mygdx.game.enums.GameState.GAME_PAUSED;
import static com.mygdx.game.enums.GameState.GAME_READY;
import static com.mygdx.game.enums.GameState.GAME_RUNNING;
import static com.mygdx.game.enums.WorldState.WORLD_STATE_GAME_OVER;
import static com.mygdx.game.enums.WorldState.WORLD_STATE_NEXT_LEVEL;
import static com.mygdx.game.utils.Settings.actualLevel;
import static com.mygdx.game.utils.Settings.levelMax;
import static java.lang.StrictMath.max;

public class GameScreen extends ScreenAdapter {

    private final RenderingSystem renderingSystem;
    private final DebugSystem debugSystem;

    SuperCubito game;

    OrthographicCamera guiCam;
    Vector3 touchPoint;
    World world;
    CollisionSystem.CollisionListener collisionListener;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    PooledEngine engine;

    private Pixmap level;

    private GameState state;

    public GameScreen(SuperCubito game, Pixmap pixmap) {
        this.game = game;
        level = pixmap;
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

        world = new World(engine, pixmap);

        engine.addSystem(new CameraSystem());
        engine.addSystem(new BackgroundSystem());
        engine.addSystem(new InputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new CollisionSystem(world, collisionListener, pixmap));
        engine.addSystem(new PlayerSystem());
        renderingSystem = new RenderingSystem(game.batcher);
        engine.addSystem(renderingSystem);
        engine.addSystem(new EnemySystem());
        engine.addSystem(new TransformerEntitiesSystem());

        debugSystem = new DebugSystem();
        engine.addSystem(debugSystem);

        engine.getSystem(BackgroundSystem.class).setCamera(engine.getSystem(RenderingSystem.class).getCamera());

        world.create();

        pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 36, 192, 36);
        quitBounds = new Rectangle(160 - 96,0, 192, 36);
    }

    public void update (float deltaTime) {
        engine.update(deltaTime);

        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning();
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

    private void updateRunning () {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            checkPauseButton();
        }

        if (world.state.equals(WORLD_STATE_NEXT_LEVEL)) {
            actualLevel +=1;
            levelMax = max(levelMax, actualLevel);
            Settings.save();
            Pixmap nextLevel = Assets.getLevel(actualLevel);
            game.analytic.nextLevel(actualLevel);
            game.setScreen(new GameScreen(game, nextLevel));
        }

        if (world.state.equals(WORLD_STATE_GAME_OVER)) {
            state = GAME_OVER;
            pauseSystems();
            Settings.save();
        }
    }

    private void checkPauseButton() {
        if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
            Assets.playSound(Assets.clickSound);
            state = GAME_PAUSED;
            pauseSystems();
        }
    }

    private void updatePaused () {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            checkResumeButton();

            checkQuitButton();
        }
    }

    private void checkQuitButton() {
        if (quitBounds.contains(touchPoint.x, touchPoint.y)) {
            Assets.playSound(Assets.clickSound);
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void checkResumeButton() {
        if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
            Assets.playSound(Assets.clickSound);
            state = GAME_RUNNING;
            resumeSystems();
        }
    }

    private void updateLevelEnd () {
        if (Gdx.input.justTouched()) {
            engine.removeAllEntities();
            world = new World(engine, level);
            state = GAME_READY;
        }
    }

    private void updateGameOver () {
        if (Gdx.input.justTouched()) {
            game.adInterface.showAd();
            game.analytic.gameOver(actualLevel);
            game.setScreen(new GameScreen(game, level));
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
        renderMissingCoins();
        game.batcher.draw(Assets.pause, 320 - 64, 480 - 64, 64, 64);
    }

    private void renderMissingCoins() {
        int count = engine.getSystem(CollisionSystem.class).totalCoins();
        Assets.yellowFont.draw(game.batcher, "Missing coins: " + count, 0, 460);
    }

    private void presentPaused () {
        game.batcher.draw(Assets.pauseMenu, 160 - 192 / 2, 0 / 2, 192, 96);
    }

    private void presentGameOver () {
        game.batcher.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
    }

    private static final Class[] PAUSABLE_SYSTEMS = new Class[] {
            PlayerSystem.class,
            QuadTreeSystem.class,
            CollisionSystem.class,
            MovementSystem.class,
            BoundsSystem.class,
            EnemySystem.class,
            InputSystem.class,
            TransformerEntitiesSystem.class
    };

    private void setPausableSystemsProcessing(final boolean shouldKeepProcessing) {
        for (Class system : PAUSABLE_SYSTEMS) {
            engine.getSystem(system).setProcessing(shouldKeepProcessing);
        }
    }

    private void pauseSystems() {
        setPausableSystemsProcessing(false);
    }

    private void resumeSystems() {
        setPausableSystemsProcessing(true);
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

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        renderingSystem.resize(width,height);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }
}
