package com.mygdx.game.levels.transformers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.utils.Assets;

public class LinearEnemyTransformer extends EntityTransformer {

    public LinearEnemyTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
    }

    @Override
    public void createEntity(float posX, float posY) {
    }

    public Entity createLinearEnemy(float posX, float posY, Vector2 speed) {
        Entity entity = engine.createEntity();

        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        movement.velocity.set(speed);

        texture.region = Assets.enemy;

        bounds.bounds.width = EnemyComponent.WIDTH;
        bounds.bounds.height = EnemyComponent.HEIGHT;

        position.pos.set(posX, posY, 3.0f);

        entity.add(enemy);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(texture);

        return entity;
    }
}
