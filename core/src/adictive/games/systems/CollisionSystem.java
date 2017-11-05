package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import adictive.games.SquareWorld;
import adictive.games.components.BoundsComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;
import adictive.games.play.PlayScreen;

public class CollisionSystem extends EntitySystem {

    private static final byte EMPTY  = 0b00000000;
    private static final byte WALL   = 0b00000001;

    private static final float PADDING = 0.01f;


    private final Family boundsFamily = Family.all(BoundsComponent.class, TransformComponent.class).get();

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<BoundsComponent> boundsMapper = ComponentMapper.getFor(BoundsComponent.class);

    private final byte[][] collisionMap;
    private PlayScreen screen;
    private final List<Entity> enemies = new ArrayList<>();
    private Entity player;

    public CollisionSystem(SquareWorld world, PlayScreen screen) {
        super();
        collisionMap = new byte[world.getWidth()][world.getHeight()];
        this.screen = screen;
    }

    @Override
    public void update(float deltaTime) {
        checkWallCollisionAndRespond();
        checkEnemyCollision();
    }

    private void checkEnemyCollision() {
        final TransformComponent playerTr = transformMapper.get(player);
        final BoundsComponent playerBc = boundsMapper.get(player);

        for (Entity enemy : enemies) {
            final TransformComponent enemyTr = transformMapper.get(enemy);
            final BoundsComponent enemyBc = boundsMapper.get(enemy);

            if (playerTr.pos.x < enemyTr.pos.x + enemyBc.bounds.width
                    && playerTr.pos.x + playerBc.bounds.width > enemyTr.pos.x
                    && playerTr.pos.y < enemyTr.pos.y + enemyBc.bounds.height
                    && playerTr.pos.y + playerBc.bounds.height > enemyTr.pos.y){

                screen.restart();
                break;
            }
        }
    }

    private void checkWallCollisionAndRespond() {
        final TransformComponent playerTr = transformMapper.get(player);
        final BoundsComponent playerBc = boundsMapper.get(player);

        checkVertexAndRespond(playerTr, playerBc,  0,  0);
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width,  0);
        checkVertexAndRespond(playerTr, playerBc,  0, playerBc.bounds.height);
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width, playerBc.bounds.height);

    }

    private void checkVertexAndRespond(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        float changeX = checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY);
        float changeY = checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY);

        if (Math.abs(changeX - playerTr.lastPos.x) < Math.abs(changeY - playerTr.lastPos.y)) {
            playerTr.pos.x = changeX;
            playerTr.pos.y = checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY);
        } else {
            playerTr.pos.y = changeY;
            playerTr.pos.x = checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY);
        }
    }

    private float checkCollisionAxisX(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        final int cellX = (int) (playerTr.pos.x + deltaX);
        final int cellY = (int) (playerTr.pos.y + deltaY);

        if (collisionMap[cellX][cellY] == WALL && (int) (playerTr.lastPos.x + deltaX) != cellX) {
            if (playerTr.lastPos.x < playerTr.pos.x) {
                return cellX - playerBc.bounds.width - PADDING;
            } else if (playerTr.lastPos.x > playerTr.pos.x) {
                return cellX + 1 + PADDING;
            }
        }
        return playerTr.pos.x;
    }

    private float checkCollisionAxisY(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        final int cellX = (int) (playerTr.pos.x + deltaX);
        final int cellY = (int) (playerTr.pos.y + deltaY);

        if (collisionMap[cellX][cellY] == WALL && (int)(playerTr.lastPos.y + deltaY) != cellY) {
            if (playerTr.lastPos.y < playerTr.pos.y) {
                return cellY - playerBc.bounds.height - PADDING;
            } else if (playerTr.lastPos.y > playerTr.pos.y) {
                return  cellY + 1 + PADDING;
            }
        }
        return playerTr.pos.y;
    }

    @Override
    public void addedToEngine(final Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(boundsFamily, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                if (entity.getComponent(WallComponent.class) != null) {
                    Vector3 pos = transformMapper.get(entity).pos;
                    collisionMap[(int)pos.x][(int)pos.y] = WALL;
                }

                if (entity.getComponent(EnemyComponent.class) != null) {
                    enemies.add(entity);
                }

                if (entity.getComponent(PlayerComponent.class) != null) {
                    player = entity;
                }
            }

            @Override
            public void entityRemoved(Entity entity) {
                if (entity.getComponent(WallComponent.class) != null) {
                    Vector3 pos = transformMapper.get(entity).pos;
                    collisionMap[(int)pos.x][(int)pos.y] = EMPTY;
                }

                if (entity.getComponent(EnemyComponent.class) != null) {
                    enemies.remove(entity);
                }

                if (entity.getComponent(PlayerComponent.class) != null) {
                    player = null;
                }
            }
        });
    }
}
