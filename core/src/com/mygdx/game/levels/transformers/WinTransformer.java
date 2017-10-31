package com.mygdx.game.levels.transformers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.WinComponent;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.systems.RenderingSystem.PIXELS_TO_METERS;

public class WinTransformer extends EntityTransformer {

    public WinTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = -140769025;
    }

    @Override
    public void createEntity(float posX, float posY) {

        Entity entity = engine.createEntity();
        WinComponent winBlock = engine.createComponent(WinComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.winBlock;

        texture.region.setRegionWidth((int) (WinComponent.WIDTH / PIXELS_TO_METERS));

        texture.region.setRegionHeight((int) (WinComponent.HEIGHT / PIXELS_TO_METERS));

        bounds.bounds.width = WinComponent.WIDTH;
        bounds.bounds.height = WinComponent.HEIGHT;

        position.pos.set(posX, posY, 3.0f);
        position.nextPosition.set(posX, posY);

        entity.add(winBlock);
        entity.add(bounds);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }
}
