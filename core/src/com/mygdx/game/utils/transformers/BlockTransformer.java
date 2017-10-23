package com.mygdx.game.utils.transformers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.mygdx.game.components.BlockComponent;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.TagComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.enums.TagEntity;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.systems.RenderingSystem.PIXELS_TO_METERS;

public class BlockTransformer extends EntityTransformer {

    public BlockTransformer(PooledEngine engine, EntityTransformer next) {
        super(engine, next);
        color = 255;
    }

    @Override
    public void createEntity(float posX, float posY) {

        Entity entity = engine.createEntity();
        BlockComponent block = engine.createComponent(BlockComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TagComponent tag = engine.createComponent(TagComponent.class);

        texture.region = Assets.wallblock;

        tag.tag = TagEntity.BLOCK;

        texture.region.setRegionWidth((int) (BlockComponent.WIDTH / PIXELS_TO_METERS));

        texture.region.setRegionHeight((int) (BlockComponent.HEIGHT / PIXELS_TO_METERS));

        bounds.bounds.width = BlockComponent.WIDTH;
        bounds.bounds.height = BlockComponent.HEIGHT;


        position.pos.set(posX, posY, 3.0f);

        entity.add(tag);
        entity.add(block);
        entity.add(bounds);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }
}
