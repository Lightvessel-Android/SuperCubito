package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
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
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utils.Assets;

public class World {
    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;

    public float heightSoFar;
    public int score;
    public int state;

    private PooledEngine engine;

    public World (PooledEngine engine) {
        this.engine = engine;
    }

    public void create() {
        Entity player = createPlayer(200,100);
        createCamera(player);
        createBackground();
        generateLevel();

        heightSoFar = 0;
        score = 0;
        state = WORLD_STATE_RUNNING;
    }

    private void generateLevel () {
    //DEBERIA CARGAR EL BITMAP ACA

        createCoin(220,100);
        createEnemy(230,150);


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
}
