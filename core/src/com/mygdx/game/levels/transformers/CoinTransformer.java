package com.mygdx.game.levels.transformers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.utils.Assets;

public class CoinTransformer extends EntityTransformer {

    public CoinTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = -420001025;
    }

    @Override
    public void createEntity(float posX, float posY) {

        Entity entity = engine.createEntity();
        CoinComponent coin = engine.createComponent(CoinComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.coin;

        bounds.bounds.width = CoinComponent.WIDTH;
        bounds.bounds.height = CoinComponent.HEIGHT;

        position.pos.set(posX, posY, 3.0f);
        position.nextPosition.set(posX, posY);

        state.set(CoinComponent.STATE_NORMAL);

        entity.add(coin);
        entity.add(bounds);
        entity.add(position);
        entity.add(texture);
        entity.add(state);

        engine.addEntity(entity);
    }
}
