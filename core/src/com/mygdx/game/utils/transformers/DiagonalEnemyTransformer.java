package com.mygdx.game.utils.transformers;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.EnemyComponent;

public class DiagonalEnemyTransformer extends LinearEnemyTransformer {

    public DiagonalEnemyTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = 65535;
    }

    @Override
    public void createEntity(float posX, float posY) {
        engine.addEntity(createLinearEnemy(posX, posY, new Vector2(EnemyComponent.VELOCITY / 2, EnemyComponent.VELOCITY / 2)));
    }
}
