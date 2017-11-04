package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import adictive.games.components.MovementComponent;
import adictive.games.components.TransformComponent;

public class MovementSystem extends IteratingSystem {
    private final Vector2 tmp = new Vector2();

    private final ComponentMapper<TransformComponent> tm;
    private final ComponentMapper<MovementComponent> mm;

    public MovementSystem() {
        super(Family.all(TransformComponent.class, MovementComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = tm.get(entity);
        MovementComponent movementComponent = mm.get(entity);

        transformComponent.lastPos.set(transformComponent.pos);

        tmp.set(movementComponent.accel).scl(deltaTime);
        movementComponent.velocity.add(tmp);

        tmp.set(movementComponent.velocity).scl(deltaTime);
        transformComponent.pos.add(tmp.x, tmp.y, 0.0f);
    }
}