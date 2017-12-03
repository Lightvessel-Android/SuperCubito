package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import adictive.games.components.MovementComponent;
import adictive.games.components.PlayerComponent;


public class PlayerInputSystem extends EntitySystem {
    private final Family family = Family.all(PlayerComponent.class, MovementComponent.class).get();
    private final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(MovementComponent.class);

    private final Vector3 touch = new Vector3(0, 0, 0);
    private final Vector3 touchOrigin = new Vector3(-1, -1, -1);

    @Override
    public void update(float deltaTime) {
        final Entity player = getEngine().getEntitiesFor(family).first();

        final MovementComponent movementComponent = movementMapper.get(player);

        if (Gdx.input.justTouched()) {
            touchOrigin.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
        }

        if (Gdx.input.isTouched() && touchOrigin.z != -1) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0f);

            touch.sub(touchOrigin.x, touchOrigin.y, 0).nor();

            movementComponent.accel.set(touch.x, -touch.y).nor().scl(PlayerComponent.ACCEL);
        } else {
            movementComponent.accel.set(0,0);
        }

    }
}
