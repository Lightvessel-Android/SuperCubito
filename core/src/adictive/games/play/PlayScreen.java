package adictive.games.play;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;

import adictive.games.SquareGame;
import adictive.games.SquareWorld;
import adictive.games.components.CoinComponent;
import adictive.games.level.LevelLoader;
import adictive.games.systems.BlackHoleSystem;
import adictive.games.systems.CollisionSystem;
import adictive.games.systems.DebugSystem;
import adictive.games.systems.DesignerSystem;
import adictive.games.systems.EnemySystem;
import adictive.games.systems.FollowCameraSystem;
import adictive.games.systems.LightSystem;
import adictive.games.systems.MovementSystem;
import adictive.games.systems.PlayerInputSystem;
import adictive.games.systems.RenderingSystem;
import adictive.games.utils.GameData;

public class PlayScreen extends ScreenAdapter {
    private static final Family COINS_FAMILY = Family.all(CoinComponent.class).get();
    private final PooledEngine engine = new PooledEngine();
    private final SquareWorld world = new SquareWorld();
    public final SquareGame superCubito;
    public final int level;

    public PlayScreen(SquareGame superCubito, int level) {
        this.superCubito = superCubito;
        this.level = level;
        initialize();
    }

    private void initialize() {
        generateSystems();
        pause(true);
        loadLevel();
        pause(false);
    }

    public void generateSystems() {
        engine.addSystem(new FollowCameraSystem());

        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new EnemySystem());
        engine.addSystem(new BlackHoleSystem(world));
        engine.addSystem(new MovementSystem());
        engine.addSystem(new CollisionSystem(world, this));
        engine.addSystem(new RenderingSystem(world));
        engine.addSystem(new LightSystem(world));
        engine.addSystem(new DebugSystem(world, this));
        engine.addSystem(new DesignerSystem(world, this));

        getDesignerSystem().setProcessing(false);
        getDebugSystem().setProcessing(false);

    }

    public void loadLevel() {
        new LevelLoader( level,world, engine).load();
    }

    public void restart() {
        superCubito.setScreen(new PlayScreen(superCubito, level));
    }

    public void win() {
        if (engine.getEntitiesFor(COINS_FAMILY).size() == 0 ) {
            superCubito.goToLevel(GameData.incrementCurrentLevel());
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        engine.getSystem(RenderingSystem.class).resize(width,height);
        engine.getSystem(LightSystem.class).resize(width,height);
    }

    public void updateMode() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            getDebugSystem().setProcessing(!getDebugSystem().checkProcessing());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            DesignerSystem designerSystem = getDesignerSystem();
            boolean isDesignerActive = !designerSystem.checkProcessing();
            designerSystem.setProcessing(isDesignerActive);
            pause(isDesignerActive);
        }
    }

    private void pause(boolean pause) {
        engine.getSystem(MovementSystem.class).setProcessing(!pause);
        engine.getSystem(CollisionSystem.class).setProcessing(!pause);
        engine.getSystem(FollowCameraSystem.class).setProcessing(!pause);
        engine.getSystem(PlayerInputSystem.class).setProcessing(!pause);
        engine.getSystem(EnemySystem.class).setProcessing(!pause);
        engine.getSystem(LightSystem.class).setProcessing(!pause);
        engine.getSystem(BlackHoleSystem.class).setProcessing(!pause);
    }

    private DebugSystem getDebugSystem() {
        return engine.getSystem(DebugSystem.class);
    }

    private DesignerSystem getDesignerSystem() {
        return engine.getSystem(DesignerSystem.class);
    }

    @Override
    public void render (float delta) {
        updateMode();
        engine.update(delta);
    }


}
