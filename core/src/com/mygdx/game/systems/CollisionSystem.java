package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.World;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.ExistComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TransformComponent;

public class CollisionSystem extends EntitySystem {
    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;

    public static interface CollisionListener {
//        public void jump ();
        public void dead ();
//        public void hit ();
        public void coin ();
    }

    private Engine engine;
    private World world;
    private CollisionListener listener;
    private ImmutableArray<Entity> exits;
    private ImmutableArray<Entity> enemies;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> coins;

    //private Random rand = new Random();
    //private ImmutableArray<Entity> squirrels;
    //private ImmutableArray<Entity> springs;
    //private ImmutableArray<Entity> castles;


    public CollisionSystem(World world, CollisionListener listener) {
        this.world = world;
        this.listener = listener;

        bm = ComponentMapper.getFor(BoundsComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, BoundsComponent.class, TransformComponent.class, StateComponent.class).get());
        coins = engine.getEntitiesFor(Family.all(CoinComponent.class, BoundsComponent.class).get());
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class, BoundsComponent.class, TransformComponent.class, StateComponent.class).get());
        exits = engine.getEntitiesFor(Family.all(ExistComponent.class, BoundsComponent.class, TransformComponent.class).get());


//        squirrels = engine.getEntitiesFor(Family.all(SquirrelComponent.class, BoundsComponent.class).get());
//        springs = engine.getEntitiesFor(Family.all(SpringComponent.class, BoundsComponent.class, TransformComponent.class).get());
//        castles = engine.getEntitiesFor(Family.all(CastleComponent.class, BoundsComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);

        for (int i = 0; i < players.size(); ++i) {
            Entity player = players.get(i);

            StateComponent playerState = sm.get(player);

            if (playerState.get() == PlayerComponent.STATE_DEAD) {
                continue;
            }

            MovementComponent playerMov = mm.get(player);
            BoundsComponent playerBounds = bm.get(player);
            TransformComponent playerPos = tm.get(player);

            for (int j = 0; j < enemies.size(); ++j) {
                    Entity spring = enemies.get(j);

                    TransformComponent springPos = tm.get(spring);
                    BoundsComponent springBounds = bm.get(spring);

                    if (playerPos.pos.y > springPos.pos.y) {
                        if (playerBounds.bounds.overlaps(springBounds.bounds)) {
                            playerSystem.dead(player);
                            listener.dead();
                        }
                    }
                }

            for (int j = 0; j < coins.size(); ++j) {
                Entity coin = coins.get(j);

                BoundsComponent coinBounds = bm.get(coin);

                if (coinBounds.bounds.overlaps(playerBounds.bounds)) {
                    engine.removeEntity(coin);
                    listener.coin();
                    world.score += CoinComponent.SCORE;
                }
            }

            for (int j = 0; j < exits.size(); ++j) {
                Entity exit = exits.get(j);

                BoundsComponent castleBounds = bm.get(exit);

                if (castleBounds.bounds.overlaps(playerBounds.bounds)) {
                    world.state = World.WORLD_STATE_NEXT_LEVEL;
                }
            }

//            if (playerMov.velocity.y < 0.0f) {
//                TransformComponent bobPos = tm.get(player);
//
//                for (int j = 0; j < platforms.size(); ++j) {
//                    Entity platform = platforms.get(j);
//
//                    TransformComponent platPos = tm.get(platform);
//
//                    if (bobPos.pos.y > platPos.pos.y) {
//                        BoundsComponent platBounds = bm.get(platform);
//
//                        if (playerBounds.bounds.overlaps(platBounds.bounds)) {
//                            playerSystem.hitPlatform(player);
//                            listener.jump();
//                            if (rand.nextFloat() > 0.5f) {
//                                platformSystem.pulverize(platform);
//                            }
//
//                            break;
//                        }
//                    }
//                }
//
//
//            };

//            for (int j = 0; j < squirrels.size(); ++j) {
//                Entity squirrel = squirrels.get(j);
//
//                BoundsComponent squirrelBounds = bm.get(squirrel);
//
//                if (squirrelBounds.bounds.overlaps(playerBounds.bounds)) {
//                    playerSystem.hitSquirrel(player);
//                    listener.hit();
//                }
//            }




        }
    }
}