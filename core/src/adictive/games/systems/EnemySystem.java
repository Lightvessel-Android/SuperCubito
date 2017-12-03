package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import adictive.games.components.EnemyComponent;
import adictive.games.components.TransformComponent;

public class EnemySystem extends IteratingSystem implements Reseteable {
    private static final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private static final ComponentMapper<EnemyComponent> enemyMapper = ComponentMapper.getFor(EnemyComponent.class);

    public EnemySystem() {
        super(Family.all(EnemyComponent.class, TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        moveEnemy(entity, deltaTime);
    }

    public static void moveEnemy(Entity entity, float deltaTime) {

        final EnemyComponent ec = enemyMapper.get(entity);

        final float nextPos = ec.posInLine + ec.vel * deltaTime;

        ec.resetDirection();

        if (nextPos > ec.direction.len() || nextPos < 0) {
            ec.vel *= -1;
            moveEnemyToPos(entity, ec, ec.posInLine + ec.vel * deltaTime);
        } else {
            moveEnemyToPos(entity, ec, nextPos);
        }
    }

    public static void moveEnemyToPos(Entity entity, EnemyComponent ec, float nextPos) {
        final TransformComponent tc = transformMapper.get(entity);
        tc.pos.set(ec.start.x, ec.start.y, 0f);
        ec.direction.nor();
        ec.direction.scl(nextPos);
        ec.posInLine = nextPos;
        tc.pos.add(ec.direction.x - tc.size.x/2, ec.direction.y - tc.size.y/2, 0f);
    }

    @Override
    public void reset() {
        // Do nothing. Does not hold game state
    }
}
