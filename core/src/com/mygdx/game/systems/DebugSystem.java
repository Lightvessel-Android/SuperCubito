package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.BoundsComponent;


public class DebugSystem extends EntitySystem {

    ShapeRenderer shapeRenderer;
    private Engine engine;
    private final Family boundables = Family.all(BoundsComponent.class).get();
    private final ComponentMapper<BoundsComponent> boundableComponent = ComponentMapper.getFor(BoundsComponent.class);

    public boolean activated = false;

    public DebugSystem() {
        super(10);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (activated) {
            OrthographicCamera camera = getEngine().getSystem(RenderingSystem.class).getCamera();
            camera.translate(0f,0f);

            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            getEngine().getSystem(CollisionStructureSystem.class).collisionStructure.render(shapeRenderer);

//            shapeRenderer.setColor(Color.BLUE);
//
//            ImmutableArray<Entity> boundableEntities = getEngine().getEntitiesFor(boundables);
//            for (Entity c : boundableEntities) {
//                Rectangle bounds = boundableComponent.get(c).bounds;
//                shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
//            }

            shapeRenderer.end();
        }
    }

    public void renderEntity(Entity entity){
        OrthographicCamera camera = getEngine().getSystem(RenderingSystem.class).getCamera();
        camera.translate(0f,0f);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);

        Rectangle bounds = entity.getComponent(BoundsComponent.class).bounds;
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        shapeRenderer.end();
    }
}
