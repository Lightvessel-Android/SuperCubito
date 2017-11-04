package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector3;

import adictive.games.components.BoundsComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;

public class CollisionSystem extends EntitySystem {

    private static final byte WALL = 0x01;
    private static final byte EMPTY = 0x00;

    private static final float PADDING = 0.01f;


    private final Family wallFamily = Family.all(WallComponent.class, BoundsComponent.class, TransformComponent.class).get();
    private final Family playerFamily = Family.all(PlayerComponent.class, BoundsComponent.class, TransformComponent.class).get();

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<BoundsComponent> boundsMapper = ComponentMapper.getFor(BoundsComponent.class);

    private final byte[][] collisionMap = new byte[64][64];

    public CollisionSystem() {
        super();
    }

    @Override
    public void update(float deltaTime) {
        checkCollisionAndRespond();
    }

    private void checkCollisionAndRespond() {
        final Entity player = getEngine().getEntitiesFor(playerFamily).first();
        final TransformComponent playerTr = transformMapper.get(player);
        final BoundsComponent playerBc = boundsMapper.get(player);

        checkVertexAndRespond(playerTr, playerBc,  0,  0);
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width,  0);
        checkVertexAndRespond(playerTr, playerBc,  0, playerBc.bounds.height);
        checkVertexAndRespond(playerTr, playerBc, playerBc.bounds.width, playerBc.bounds.height);

    }

    private void checkVertexAndRespond(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        if (Math.abs(playerTr.lastPos.x - playerTr.pos.x) > Math.abs(playerTr.lastPos.y - playerTr.pos.y)) {
            checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY);
            checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY);
        } else {
            checkCollisionAxisY(playerTr, playerBc, deltaX, deltaY);
            checkCollisionAxisX(playerTr, playerBc, deltaX, deltaY);
        }
    }

    private void checkCollisionAxisX(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        int cellX = (int) (playerTr.pos.x + deltaX);
        int cellY = (int) (playerTr.pos.y + deltaY);

        if (collisionMap[cellX][cellY] == WALL) {

            if ((int) (playerTr.lastPos.x + deltaX) != cellX) {
                if (playerTr.lastPos.x < playerTr.pos.x) {
                    playerTr.pos.x = cellX - playerBc.bounds.width - PADDING;
                } else if (playerTr.lastPos.x > playerTr.pos.x) {
                    playerTr.pos.x = cellX + 1 + PADDING;
                }
            }
        }
    }

    private void checkCollisionAxisY(TransformComponent playerTr, BoundsComponent playerBc, float deltaX, float deltaY) {
        int cellX;
        int cellY;
        cellX = (int) (playerTr.pos.x + deltaX);
        cellY = (int) (playerTr.pos.y + deltaY);

        if (collisionMap[cellX][cellY] == WALL) {
            if ((int)(playerTr.lastPos.y + deltaY) != cellY) {
                if (playerTr.lastPos.y < playerTr.pos.y) {
                    playerTr.pos.y = cellY - playerBc.bounds.height - PADDING;
                } else if (playerTr.lastPos.y > playerTr.pos.y) {
                    playerTr.pos.y = cellY + 1 + PADDING;
                }
            }
        }
    }

    @Override
    public void addedToEngine(final Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(wallFamily, new EntityListener() {
            @Override
            public void entityAdded(Entity entity) {
                if (entity.getComponent(WallComponent.class) != null) {
                    Vector3 pos = transformMapper.get(entity).pos;
                    collisionMap[(int)pos.x][(int)pos.y] = WALL;
                }
            }

            @Override
            public void entityRemoved(Entity entity) {
                if (entity.getComponent(WallComponent.class) != null) {
                    Vector3 pos = transformMapper.get(entity).pos;
                    collisionMap[(int)pos.x][(int)pos.y] = EMPTY;
                }
            }
        });
    }
}
