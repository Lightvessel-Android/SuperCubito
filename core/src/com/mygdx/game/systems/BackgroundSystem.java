package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.components.BackgroundComponent;

public class BackgroundSystem extends IteratingSystem {

    private OrthographicCamera camera;

    public BackgroundSystem() {
        super(Family.all(BackgroundComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }
}
