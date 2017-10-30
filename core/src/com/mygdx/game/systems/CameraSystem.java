package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.CameraComponent;
import com.mygdx.game.components.TransformComponent;

public class CameraSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<CameraComponent> cm;
    private CameraComponent cameraComponent;

    public CameraSystem() {
        super(Family.all(CameraComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        cm = ComponentMapper.getFor(CameraComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        CameraComponent cam = cm.get(entity);

        cameraComponent = cam;

        if (cam.target == null) {
            return;
        }

        TransformComponent target = tm.get(cam.target);

        if (target == null) {
            return;
        }

        cam.camera.position.x =target.pos.x;
        cam.camera.position.y =target.pos.y;
    }

    public void unproject(Vector3 set) {
        cameraComponent.camera.unproject(set);
    }

    public void addZoom(float delta) {
        cameraComponent.camera.zoom +=delta;
    }

    public CameraComponent getCameraComponent() {
        return cameraComponent;
    }
}
