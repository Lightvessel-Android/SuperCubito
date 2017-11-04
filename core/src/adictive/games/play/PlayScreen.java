package adictive.games.play;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import adictive.games.SquareWorld;
import adictive.games.level.LevelLoader;
import adictive.games.systems.CollisionSystem;
import adictive.games.systems.DebugSystem;
import adictive.games.systems.FollowCameraSystem;
import adictive.games.systems.MovementSystem;
import adictive.games.systems.PlayerInputSystem;
import adictive.games.systems.RenderingSystem;

public class PlayScreen extends ScreenAdapter {
    private final PooledEngine engine = new PooledEngine();
    private final SquareWorld world = new SquareWorld();
    private final SpriteBatch batcher = new SpriteBatch();

    public PlayScreen() {
        generateSystems();
        loadLevel();
    }

    public void generateSystems() {
        engine.addSystem(new FollowCameraSystem());

        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new RenderingSystem(world, batcher));
        engine.addSystem(new DebugSystem(world));
    }

    public void loadLevel() {
        LevelLoader.load(world, engine);
    }

    @Override
    public void render (float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        engine.getSystem(RenderingSystem.class).resize(width,height);
    }

}
