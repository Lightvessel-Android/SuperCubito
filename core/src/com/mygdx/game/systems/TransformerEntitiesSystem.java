package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.ExpandComponent;
import com.mygdx.game.components.TransformComponent;

public class TransformerEntitiesSystem extends EntitySystem {

    private ImmutableArray<Entity> expandEnemies;

    private ComponentMapper<TransformComponent> tm;

    private float timeOfLoop;
    private float elapsed;

    private Engine engine;

    public TransformerEntitiesSystem(){
        tm = ComponentMapper.getFor(TransformComponent.class);
        timeOfLoop = 1f;
        elapsed = 0f;
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        expandEnemies = engine.getEntitiesFor(Family.all(EnemyComponent.class, BoundsComponent.class, TransformComponent.class, ExpandComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {

        if(elapsed >= timeOfLoop){

            for (int j = 0; j < expandEnemies.size(); ++j) {
                Entity enemy = expandEnemies.get(j);

                TransformComponent transform = tm.get(enemy);

                transform.scale.set(1,1);
            }

            elapsed = 0f;

        } else {
            for (int j = 0; j < expandEnemies.size(); ++j) {
                Entity enemy = expandEnemies.get(j);
                TransformComponent transform = tm.get(enemy);

                float newScaleX = (elapsed * ExpandComponent.max_scale_x) / timeOfLoop;
                float newScaleY = (elapsed * ExpandComponent.max_scale_y) / timeOfLoop;

                transform.scale.set(newScaleX, newScaleY);
            }

        }

        elapsed += deltaTime;

    }

}
