package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;

import adictive.games.components.AttractionComponent;
import adictive.games.components.BlackHoleComponent;
import adictive.games.components.MovementComponent;
import adictive.games.components.TransformComponent;

public class BlackHoleSystem extends IteratingSystem implements Reseteable {
    private static final ComponentMapper<MovementComponent> movementComponent = ComponentMapper.getFor(MovementComponent.class);
    private static final ComponentMapper<BlackHoleComponent> holeMapper = ComponentMapper.getFor(BlackHoleComponent.class);
    private static final Family HOLE_FAMILY = Family.all(BlackHoleComponent.class, TransformComponent.class).get();
    private static final Family ATTRACTABLE_FAMILY = Family.all(AttractionComponent.class, MovementComponent.class).get();

    private final Vector2 tmp = new Vector2();

    public BlackHoleSystem() {
        super(HOLE_FAMILY);
    }

    @Override
    protected void processEntity(Entity attractor, float deltaTime) {
        final ImmutableArray<Entity> attractables = getEngine().getEntitiesFor(ATTRACTABLE_FAMILY);
        final TransformComponent tc = attractor.getComponent(TransformComponent.class);
        final float centerX = tc.pos.x + tc.size.x / 2;
        final float centerY = tc.pos.y + tc.size.y / 2;
        final float attraction = holeMapper.get(attractor).attraction;

        for (Entity e: attractables) {
            attract(e, centerX, centerY, attraction, deltaTime);
        }
    }

    private void attract(Entity entity, float centerX, float centerY, float attraction, float deltaTime) {
        final MovementComponent mv = movementComponent.get(entity);
        final TransformComponent tc = entity.getComponent(TransformComponent.class);
        tmp.set(centerX, centerY);
        tmp.sub(tc.pos.x + tc.size.x/2 , tc.pos.y+ tc.size.y/2);
        float distance = tmp.len();
        tmp.nor().scl(attraction + (float) Math.pow(1 + attraction/distance,3)).scl(deltaTime);
        mv.velocity.add(tmp);
    }

    @Override
    public void reset() {
        // Do nothing. This system does not hold state
    }
}
