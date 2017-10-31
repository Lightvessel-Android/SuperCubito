package com.mygdx.game.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.World;
import com.mygdx.game.components.BlockComponent;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.WinComponent;
import com.mygdx.game.utils.CollisionStructure.CollisionStructure;
import com.mygdx.game.utils.CollisionStructure.Grid;

import static com.mygdx.game.enums.WorldState.WORLD_STATE_GAME_OVER;
import static com.mygdx.game.enums.WorldState.WORLD_STATE_NEXT_LEVEL;

public class CollisionSystem extends EntitySystem {
    private ComponentMapper<StateComponent> sm;

    public interface CollisionListener {
        void dead();
        void coin();
    }

    CollisionStructure collisionStructure;
    private Engine engine;
    private World world;
    private CollisionListener listener;
    private Array<Entity> auxList;
    private ImmutableArray<Entity> enemies, players, coins, blocks, wins;
    private Array<Entity> enemiesCol;
    private boolean isFirstCycle;

    private Rectangle reqAux1, reqAux2;

    public CollisionSystem(World world, CollisionListener listener, Pixmap pixmap) {
        this.world = world;
        this.listener = listener;

        sm = ComponentMapper.getFor(StateComponent.class);
        auxList = new Array<>();
        collisionStructure = new Grid(pixmap.getWidth(),pixmap.getHeight(), 5);
        reqAux1 = new Rectangle(0,0,0,0);
        reqAux2 = new Rectangle(0,0,0,0);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, BoundsComponent.class, TransformComponent.class, StateComponent.class).get());
        coins = engine.getEntitiesFor(Family.all(CoinComponent.class, BoundsComponent.class).get());
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class, BoundsComponent.class, TransformComponent.class).get());

        wins = engine.getEntitiesFor(Family.all(WinComponent.class).get());
        blocks = engine.getEntitiesFor(Family.all(BlockComponent.class).get());

        enemiesCol = new Array<>();
        isFirstCycle = true;
    }

    private void addEntitiesToGrid(ImmutableArray<Entity> entities) {
        for (int i = 0; i < entities.size(); i++) {
            collisionStructure.insert(entities.get(i));
        }
    }

    @Override
    public void update(float deltaTime) {
        PlayerSystem playerSystem = engine.getSystem(PlayerSystem.class);
        enemiesCol.clear();

        if(isFirstCycle) {
            addEntitiesToGrid(blocks);
            addEntitiesToGrid(wins);
            isFirstCycle = false;
        }

        for (int i = 0; i < players.size(); ++i) {
            Entity player = players.get(i);

            StateComponent playerState = sm.get(player);

            if (playerState.get() == PlayerComponent.STATE_DEAD) {
                world.state = WORLD_STATE_GAME_OVER;
                continue;
            }

            checkEnemiesCollision(playerSystem, player);

            checkCoinsCollision(player);

            checkBlocksCollision(playerSystem, player);

            checkExitsCollision(player);
        }
    }

    private void checkExitsCollision(Entity player) {
        if(coins.size() == 0 && existsCollision(player, WinComponent.class)) {
            world.state = WORLD_STATE_NEXT_LEVEL;
        }
    }

    private void checkBlocksCollision(PlayerSystem playerSystem, Entity player) {

        if(existsCollision(player, BlockComponent.class)) {
            playerSystem.hitBlock(player);
        }

        for(Entity enemy : enemies){
            if(existsCollision(enemy, BlockComponent.class)) {
                if(!enemiesCol.contains(enemy, false)) {
                    enemiesCol.add(enemy);
                }
            }
        }
    }

    private void checkCoinsCollision(Entity player) {
        for (Entity coin : coins){
            if (overlaps(player, coin)){
                engine.removeEntity(coin);
                listener.coin();
            }
        }
    }

    private boolean overlaps(Entity entity1, Entity entity2){
        return entity1.getComponent(BoundsComponent.class).bounds.overlaps(entity2.getComponent(BoundsComponent.class).bounds);
    }

    private boolean nextPositionOverlaps(Entity entity1, Entity entity2) {
        TransformComponent entityTransform1 = entity1.getComponent(TransformComponent.class);
        TransformComponent entityTransform2 = entity2.getComponent(TransformComponent.class);

        reqAux1 = entity1.getComponent(BoundsComponent.class).bounds;
        reqAux1.setPosition(entityTransform1.nextPosition);

        reqAux2 = entity2.getComponent(BoundsComponent.class).bounds;
        reqAux2.setPosition(entityTransform2.nextPosition);

        return reqAux1.overlaps(reqAux2);
    }

    private void checkEnemiesCollision(PlayerSystem playerSystem, Entity player) {
        for (Entity enemy: enemies) {
            if(overlaps(player, enemy)){
                playerSystem.dead(player);
                listener.dead();
            }
        }
    }

    private boolean existsCollision(Entity entity, Class<? extends  Component> component){
        auxList.clear();
        collisionStructure.retrieve(auxList, entity);

        for (Entity col : auxList){
            if(nextPositionOverlaps(entity, col) && col.getComponent(component) != null){
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