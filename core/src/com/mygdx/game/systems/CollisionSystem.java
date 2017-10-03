package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.World;
import com.mygdx.game.components.BlockComponent;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.WinComponent;

import static com.mygdx.game.states.WorldState.WORLD_STATE_GAME_OVER;
import static com.mygdx.game.states.WorldState.WORLD_STATE_NEXT_LEVEL;

public class CollisionSystem extends EntitySystem {
    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;

    public static interface CollisionListener {
        public void dead ();
        public void coin ();
    }

    private Engine engine;
    private World world;
    private CollisionListener listener;
    private Vector2 vector;

    private ImmutableArray<Entity> exits, enemies, players, coins, blocks;

    private Array<Entity> enemiesCol;


    public CollisionSystem(World world, CollisionListener listener) {
        this.world = world;
        this.listener = listener;

        bm = ComponentMapper.getFor(BoundsComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        vector = new Vector2(0, 0);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, BoundsComponent.class, TransformComponent.class, StateComponent.class).get());
        coins = engine.getEntitiesFor(Family.all(CoinComponent.class, BoundsComponent.class).get());
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class, BoundsComponent.class, TransformComponent.class).get());
        blocks = engine.getEntitiesFor(Family.all(BlockComponent.class, BoundsComponent.class).get());
        exits = engine.getEntitiesFor(Family.all(WinComponent.class, BoundsComponent.class, TransformComponent.class).get());

        enemiesCol = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);

        enemiesCol.clear();

        for (int i = 0; i < players.size(); ++i) {
            Entity player = players.get(i);

            StateComponent playerState = sm.get(player);

            if (playerState.get() == PlayerComponent.STATE_DEAD) {
                world.state = WORLD_STATE_GAME_OVER;
                continue;
            }

            checkEnemiesCollision(playerSystem, player);

            checkCoinsCollision(player);

            checkBlocksCollision(playerSystem, player, deltaTime);

            checkExitsCollision(player);
        }
    }

    private void checkExitsCollision(Entity player) {
        for (int j = 0; j < exits.size(); ++j) {
            Entity exit = exits.get(j);

            if (isCollide(exit, player) && coins.size() == 0) {
                world.state = WORLD_STATE_NEXT_LEVEL;
            }
        }
    }

    private void checkBlocksCollision(PlayerSystem playerSystem, Entity player, float deltaTime) {
        for (int j = 0; j < blocks.size(); ++j) {
            Entity block = blocks.get(j);


            if (isCollide(block, player)) {
                playerSystem.hitBlock(player);
            }

            for (int k = 0; k < enemies.size(); ++k) {
                Entity enemy = enemies.get(k);

                if (isNextPositionColide(enemy, block, deltaTime)) {
                    if(!enemiesCol.contains(enemy, false)) {
                        enemiesCol.add(enemy);
                    }
                }
            }
        }
    }

    private void checkCoinsCollision(Entity player) {
        for (int j = 0; j < coins.size(); ++j) {
            Entity coin = coins.get(j);

            if (isCollide(coin, player)) {
                engine.removeEntity(coin);
                listener.coin();
            }
        }
    }

    private void checkEnemiesCollision(PlayerSystem playerSystem, Entity player) {
        for (int j = 0; j < enemies.size(); ++j) {
            Entity enemy = enemies.get(j);

            if (isCollide(enemy, player)) {
                playerSystem.dead(player);
                listener.dead();
            }
        }
    }

    private boolean isNextPositionColide(Entity enemy, Entity block, float delta) {
        BoundsComponent bounds1 = bm.get(block);
        BoundsComponent bounds2 = bm.get(enemy);
        MovementSystem movementSystem = engine.getSystem(MovementSystem.class);

        Vector2 initialPos = movementSystem.nextPosition(enemy, delta);


        bounds2.bounds.setPosition(initialPos);



        return bounds1.bounds.overlaps(bounds2.bounds);
    }

    private boolean isCollide(Entity entity1, Entity entity2) {
        BoundsComponent bounds1 = bm.get(entity1);
        BoundsComponent bounds2 = bm.get(entity2);

        return bounds1.bounds.overlaps(bounds2.bounds);
    }

    public Array<Entity> getEnemiesCol() {
        return enemiesCol;
    }

    public void setEnemiesCol(Array<Entity> enemiesCol) {
        this.enemiesCol = enemiesCol;
    }

}