package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.components.BackgroundComponent;
import com.mygdx.game.components.TransformComponent;

public class BackgroundSystem extends IteratingSystem {

    private OrthographicCamera camera;
    private ComponentMapper<TransformComponent> tm;

    public BackgroundSystem() {
        super(Family.all(BackgroundComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);

    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        t.pos.set(camera.position.x, camera.position.y, 10.0f);
        t.scale.set(4f, 8f);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
}
