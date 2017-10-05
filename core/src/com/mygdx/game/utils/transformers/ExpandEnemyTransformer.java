package com.mygdx.game.utils.transformers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.ExpandComponent;

public class ExpandEnemyTransformer extends LinearEnemyTransformer {

    private Vector2 velocity;

    public ExpandEnemyTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = -1.93108378E9f;
        velocity = new Vector2(0, 0);
    }

    @Override
    public void createEntity(float posX, float posY) {

        Entity entity = createLinearEnemy(posX, posY, new Vector2(velocity.x, velocity.y));

        ExpandComponent expand = engine.createComponent(ExpandComponent.class);

        entity.add(expand);

        engine.addEntity(entity);
    }
}
