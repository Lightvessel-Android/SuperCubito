package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.components.BackgroundComponent;
import com.mygdx.game.components.CameraComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.states.WorldState;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.transformers.BlockTransformer;
import com.mygdx.game.utils.transformers.CoinTransformer;
import com.mygdx.game.utils.transformers.DiagonalEnemyTransformer;
import com.mygdx.game.utils.transformers.EntityTransformer;
import com.mygdx.game.utils.transformers.ExpandEnemyTransformer;
import com.mygdx.game.utils.transformers.HorizontalEnemyTransformer;
import com.mygdx.game.utils.transformers.PlayerTransformer;
import com.mygdx.game.utils.transformers.VerticalEnemyTransformer;
import com.mygdx.game.utils.transformers.WinTransformer;

import static com.mygdx.game.states.WorldState.WORLD_STATE_RUNNING;
import static com.mygdx.game.systems.RenderingSystem.CELL_TO_METERS;
import static com.mygdx.game.systems.RenderingSystem.PIXELS_TO_METERS;

public class World {
    public WorldState state;

    private PooledEngine engine;

    private Pixmap level;

    private Entity player;

    private EntityTransformer firstTransformer;

    public World (PooledEngine engine, Pixmap pixmap) {
        this.engine = engine;
        level = pixmap;

        BlockTransformer blockTransformer = new BlockTransformer(engine, null);
        HorizontalEnemyTransformer horizontalEnemyTransformer = new HorizontalEnemyTransformer(engine, blockTransformer);
        VerticalEnemyTransformer verticalEnemyTransformer = new VerticalEnemyTransformer(engine, horizontalEnemyTransformer);
        DiagonalEnemyTransformer diagonalEnemyTransformer = new DiagonalEnemyTransformer(engine, verticalEnemyTransformer);
        CoinTransformer coinTransformer = new CoinTransformer(engine, diagonalEnemyTransformer);
        WinTransformer winTransformer = new WinTransformer(engine, coinTransformer);

        ExpandEnemyTransformer expandEnemyTransformer = new ExpandEnemyTransformer(engine, winTransformer);

        firstTransformer = new PlayerTransformer(engine, this, expandEnemyTransformer);
    }

    public void create() {
        createBackground();
        generateLevel(level.getWidth(), level.getHeight());

        createCamera(player);

        state = WORLD_STATE_RUNNING;
    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraComponent camera = new CameraComponent();
        camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
        camera.target = target;

        entity.add(camera);

        engine.addEntity(entity);
    }

    private void createBackground() {
        Entity entity = engine.createEntity();

        BackgroundComponent background = engine.createComponent(BackgroundComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.background;

        texture.region.setRegionWidth((int) (level.getWidth() / PIXELS_TO_METERS));
        texture.region.setRegionHeight((int) (level.getHeight()  / PIXELS_TO_METERS));

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }


    private void generateLevel(int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int pixel = level.getPixel(x, y);
                float posX = x * CELL_TO_METERS;
                float posY = y * CELL_TO_METERS;

                firstTransformer.create(posX, posY, pixel);
            }
        }
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }
}
