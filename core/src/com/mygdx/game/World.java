package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.components.BackgroundComponent;
import com.mygdx.game.components.BlockComponent;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.CameraComponent;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.WinComponent;
import com.mygdx.game.states.WorldState;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.states.WorldState.WORLD_STATE_RUNNING;
import static com.mygdx.game.systems.RenderingSystem.CELL_TO_METERS;
import static com.mygdx.game.systems.RenderingSystem.PIXELS_TO_METERS;

public class World {
    public WorldState state;

    private PooledEngine engine;

    private Pixmap level;

    private Entity player;

    public World (PooledEngine engine, Pixmap pixmap) {
        this.engine = engine;
        level = pixmap;
    }

    public void create() {
        createBackground();
        generateLevel();
        createCamera(player);

        state = WORLD_STATE_RUNNING;
    }

    private Entity createPlayer(float x, float y) {
        Entity entity = engine.createEntity();

        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.player;

        bounds.bounds.width = PlayerComponent.WIDTH;
        bounds.bounds.height = PlayerComponent.HEIGHT;

        position.pos.set(x, y, 0.0f);

        state.set(PlayerComponent.STATE_ALIVE);

        entity.add(player);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(state);
        entity.add(texture);

        engine.addEntity(entity);

        return entity;
    }

    private void createCoin(float x, float y) {
        Entity entity = engine.createEntity();
        CoinComponent coin = engine.createComponent(CoinComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.coin;

        bounds.bounds.width = CoinComponent.WIDTH;
        bounds.bounds.height = CoinComponent.HEIGHT;

        position.pos.set(x, y, 3.0f);

        state.set(CoinComponent.STATE_NORMAL);

        entity.add(coin);
        entity.add(bounds);
        entity.add(position);
        entity.add(texture);
        entity.add(state);

        engine.addEntity(entity);
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


        //Habria que crear muchos
        texture.region = Assets.background;

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createBlock(float x, float y) {

        Entity entity = engine.createEntity();
        BlockComponent block = engine.createComponent(BlockComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.wallblock;


        texture.region.setRegionWidth((int) (BlockComponent.WIDTH / PIXELS_TO_METERS));

        texture.region.setRegionHeight((int) (BlockComponent.HEIGHT / PIXELS_TO_METERS));

        bounds.bounds.width = BlockComponent.WIDTH;
        bounds.bounds.height = BlockComponent.HEIGHT;


        position.pos.set(x, y, 3.0f);

        entity.add(block);
        entity.add(bounds);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createEnemy(float x, float y) {
        Entity entity = engine.createEntity();

        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        movement.velocity.x = EnemyComponent.VELOCITY;

        texture.region = Assets.enemy;

        bounds.bounds.width = EnemyComponent.WIDTH;
        bounds.bounds.height = EnemyComponent.HEIGHT;

        position.pos.set(x, y, 3.0f);

        entity.add(enemy);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createWinFloor(float x, float y) {

        Entity entity = engine.createEntity();
        WinComponent winBlock = engine.createComponent(WinComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.winBlock;

        texture.region.setRegionWidth((int) (WinComponent.WIDTH / PIXELS_TO_METERS));

        texture.region.setRegionHeight((int) (WinComponent.HEIGHT / PIXELS_TO_METERS));

        bounds.bounds.width = WinComponent.WIDTH;
        bounds.bounds.height = WinComponent.HEIGHT;


        position.pos.set(x, y, 3.0f);

        entity.add(winBlock);
        entity.add(bounds);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void generateLevel() {

        int width = level.getWidth();
        int height = level.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = level.getPixel(x, y);
                float posX = x * CELL_TO_METERS;
                float posY = y * CELL_TO_METERS;
                switch (pixel) {
                    case -824246273:
                        //Exterior
                        break;
                    case -1660965377:
                        //suelo verde
                        break;
                    case -1:
                        //suelo blanco de juego
                        break;
                    case -140769025:
                        createWinFloor(posX, posY);
                        break;
                    case 255:
                        createBlock(posX, posY);
                        break;
                    case 279280639:
                        player = createPlayer(posX, posY);
                        break;
                    case -16776961:
                        createEnemy(posX, posY);
                        break;
                    case -420001025:
                        createCoin(posX, posY);
                        break;
                    default:
                        System.out.print(pixel);
                        break;
                }
            }
        }
    }
}
