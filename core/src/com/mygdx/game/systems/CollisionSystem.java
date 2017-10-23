package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.World;
import com.mygdx.game.components.BlockComponent;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TagComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.WinComponent;
import com.mygdx.game.enums.TagEntity;

import static com.mygdx.game.enums.WorldState.WORLD_STATE_GAME_OVER;
import static com.mygdx.game.enums.WorldState.WORLD_STATE_NEXT_LEVEL;

public class CollisionSystem extends EntitySystem {
    private ComponentMapper<BoundsComponent> bm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TagComponent> tagMapper;

    public interface CollisionListener {
        void dead();
        void coin();
    }

    private Engine engine;
    private World world;
    private CollisionListener listener;
    private QuadTreeSystem quadTreeSystem;
    private Array<Entity> auxList;

    private ImmutableArray<Entity> exits, enemies, players, coins, blocks;

    private Array<Entity> enemiesCol;


    public CollisionSystem(World world, CollisionListener listener) {
        this.world = world;
        this.listener = listener;

        bm = ComponentMapper.getFor(BoundsComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tagMapper = ComponentMapper.getFor(TagComponent.class);

        auxList = new Array<Entity>();
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, BoundsComponent.class, TransformComponent.class, StateComponent.class).get());
        coins = engine.getEntitiesFor(Family.all(CoinComponent.class, BoundsComponent.class).get());
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class, BoundsComponent.class, TransformComponent.class).get());
        blocks = engine.getEntitiesFor(Family.all(BlockComponent.class, BoundsComponent.class).get());
        exits = engine.getEntitiesFor(Family.all(WinComponent.class, BoundsComponent.class, TransformComponent.class).get());

        quadTreeSystem = engine.getSystem(QuadTreeSystem.class);

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

            checkCoinsCollision();

            checkBlocksCollision(playerSystem, player, deltaTime);

            checkExitsCollision(player);
        }
    }

    private void checkExitsCollision(Entity player) {

        if(coins.size() == 0 && existsCollision(player, TagEntity.EXIT)) {
            world.state = WORLD_STATE_NEXT_LEVEL;
        }
    }

    private void checkBlocksCollision(PlayerSystem playerSystem, Entity player, float deltaTime) {
        if(existsCollision(player, TagEntity.BLOCK)) {
            playerSystem.hitBlock(player);
        }

        for(Entity enemy : enemies){
            if(existsCollision(enemy, TagEntity.BLOCK)) {
                if(!enemiesCol.contains(enemy, false)) {
                        enemiesCol.add(enemy);
                    }
            }
        }
    }

    private void checkCoinsCollision() {

        for (Entity coin : coins){
            if (existsCollision(coin, TagEntity.PLAYER)){
                engine.removeEntity(coin);
                listener.coin();
            }
        }
    }

    private void checkEnemiesCollision(PlayerSystem playerSystem, Entity player) {

        if (existsCollision(player, TagEntity.ENEMY)){
            playerSystem.dead(player);
            listener.dead();
        }
    }

    private boolean existsCollision(Entity entity, TagEntity tag){
        auxList.clear();
        quadTreeSystem.getQuadTree().retrieve(auxList, entity.getComponent(BoundsComponent.class).bounds);
        return anyHaveTag(auxList, tag);
    }

    private boolean anyHaveTag(Array<Entity> auxList, TagEntity tag) {
        for (Entity entity : auxList){
            if(tagMapper.get(entity).tag.equals(tag)){
                return true;
            }
        }
        return false;
    }

    public Array<Entity> getEnemiesCol() {
        return enemiesCol;
    }

    public int totalCoins(){
        return coins.size();
    }

}