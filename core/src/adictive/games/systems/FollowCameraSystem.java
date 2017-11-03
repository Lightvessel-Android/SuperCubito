package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import adictive.games.components.CameraComponent;
import adictive.games.components.TransformComponent;

public class FollowCameraSystem extends IteratingSystem {
    private final ComponentMapper<TransformComponent> tm;
    private final ComponentMapper<CameraComponent> cm;

    public FollowCameraSystem() {
        super(Family.all(TransformComponent.class, CameraComponent.class).get());
        cm = ComponentMapper.getFor(CameraComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final CameraComponent cam = cm.get(entity);
        final TransformComponent target = tm.get(entity);

        cam.camera.position.x = target.pos.x;
        cam.camera.position.y = target.pos.y;
    }
}
