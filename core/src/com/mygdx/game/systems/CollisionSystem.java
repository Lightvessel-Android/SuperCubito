package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
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
    }

    @Override
    public void update(float deltaTime) {
        PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);

        for (int i = 0; i < players.size(); ++i) {
            Entity player = players.get(i);

            StateComponent playerState = sm.get(player);

            if (playerState.get() == PlayerComponent.STATE_DEAD) {
                world.state = WORLD_STATE_GAME_OVER;
                continue;
            }

            MovementComponent playerMov = mm.get(player);
            BoundsComponent playerBounds = bm.get(player);
            TransformComponent playerPos = tm.get(player);

            for (int j = 0; j < enemies.size(); ++j) {
                    Entity enemy = enemies.get(j);

                    BoundsComponent enemyBounds = bm.get(enemy);

                    if (enemyBounds.bounds.overlaps(playerBounds.bounds)) {
                        playerSystem.dead(player);
                        listener.dead();
                    }
                }

            for (int j = 0; j < coins.size(); ++j) {
                Entity coin = coins.get(j);

                BoundsComponent coinBounds = bm.get(coin);

                if (coinBounds.bounds.overlaps(playerBounds.bounds)) {
                    engine.removeEntity(coin);
                    listener.coin();
                }
            }

            for (int j = 0; j < blocks.size(); ++j) {
                Entity block = blocks.get(j);

                BoundsComponent blockBounds = bm.get(block);

                if (blockBounds.bounds.overlaps(playerBounds.bounds)) {
                    vector = (blockBounds.bounds.getPosition(vector).sub(playerPos.pos.x, playerPos.pos.y)).nor();
                    //TODO: hacer que no lo traspase
                }
            }

            for (int j = 0; j < exits.size(); ++j) {
                Entity exit = exits.get(j);

                BoundsComponent castleBounds = bm.get(exit);

                if (castleBounds.bounds.overlaps(playerBounds.bounds)) {
                    world.state = WORLD_STATE_NEXT_LEVEL;
                }
            }
        }
    }
}