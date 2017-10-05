package com.mygdx.game.utils.transformers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.ExpandComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.utils.Assets;

public class ExpandEnemyTransformer extends EntityTransformer {

    public ExpandEnemyTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = -1.93108378E9f;
    }

    @Override
    public void createEntity(float posX, float posY) {

        Entity entity = engine.createEntity();

        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);

        ExpandComponent expand = engine.createComponent(ExpandComponent.class);

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);

        MovementComponent movement = engine.createComponent(MovementComponent.class);

        TransformComponent position = engine.createComponent(TransformComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);

        movement.velocity.set(0,0);

        texture.region = Assets.enemy;

        bounds.bounds.width = EnemyComponent.WIDTH;
        bounds.bounds.height = EnemyComponent.HEIGHT;

        position.pos.set(posX, posY, 3.0f);

        entity.add(enemy);
        entity.add(expand);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }
}
