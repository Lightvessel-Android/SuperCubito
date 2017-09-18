package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.BackgroundComponent;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.CameraComponent;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.states.WorldState;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.states.WorldState.WORLD_STATE_RUNNING;

public class World {
    public WorldState state;

    private PooledEngine engine;

    public World (PooledEngine engine) {
        this.engine = engine;
    }

    public void create() {
//        createCamera(player);
        createBackground();
        generateLevel(Assets.level);

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

        texture.region = Assets.background;

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createEnemy(float x, float y) {
        Entity entity = engine.createEntity();

        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        movement.velocity.x = EnemyComponent.VELOCITY;

        texture.region = Assets.enemy;

        bounds.bounds.width = EnemyComponent.WIDTH;
        bounds.bounds.height = EnemyComponent.HEIGHT;

        position.pos.set(x, y, 2.0f);

        state.set(EnemyComponent.STATE_NORMAL);

        entity.add(animation);
        entity.add(enemy);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(state);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void generateLevel(Pixmap level) {

        int width = level.getWidth();
        int height = level.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = level.getPixel(x, y);
                float posX = x * RenderingSystem.PIXELS_TO_METRES;
                float posY = y * RenderingSystem.PIXELS_TO_METRES;
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
                        //meta
                        break;
                    case 255:
//                        entities.add(new BlockEntity(blockTexture, position, new Vector2(12,12), new Vector2(0, 0)));
                        break;
                    case 279280639:
                        createPlayer(posX, posY);
                        break;
                    case -16776961:
                        createEnemy(posX, posY);
                        break;
                    case -403223809:
                        createCoin(posX, posY);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
