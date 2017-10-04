package com.mygdx.game.utils.transformers;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.EnemyComponent;

public class HorizontalEnemyTransformer extends LinearEnemyTransformer {

    public HorizontalEnemyTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = -16776961;
    }

    @Override
    public void createEntity(float posX, float posY) {
        createLinearEnemy(posX, posY, new Vector2(EnemyComponent.VELOCITY, 0));
    }
}
