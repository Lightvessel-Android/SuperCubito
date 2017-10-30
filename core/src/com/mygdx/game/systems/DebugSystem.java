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
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.PlayerComponent;


public class DebugSystem extends EntitySystem {

    ShapeRenderer shapeRenderer;
    private Engine engine;
    private final ComponentMapper<BoundsComponent> boundableComponent = ComponentMapper.getFor(BoundsComponent.class);
    private final Family players = Family.all(PlayerComponent.class).get();
    private Array<Entity> auxList;

    public boolean activated = false;

    public DebugSystem() {
        super(10);
        shapeRenderer = new ShapeRenderer();
        auxList = new Array<>();
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


            getEngine().getSystem(CollisionSystem.class).collisionStructure.retrieve(auxList, engine.getEntitiesFor(players).first());

            shapeRenderer.setColor(Color.RED);

            for (Entity c : auxList) {
                if (boundableComponent.get(c) == null) continue;
                Rectangle bounds = boundableComponent.get(c).bounds;
                shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
            }

            shapeRenderer.end();
        }
    }
}
