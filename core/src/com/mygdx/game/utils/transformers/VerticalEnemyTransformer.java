package com.mygdx.game.utils.transformers;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.EnemyComponent;

public class VerticalEnemyTransformer extends LinearEnemyTransformer {

    public VerticalEnemyTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = -175308801;
    }

    @Override
    public void createEntity(float posX, float posY) {
        engine.addEntity(createLinearEnemy(posX, posY, new Vector2(0, EnemyComponent.VELOCITY)));
    }
}
