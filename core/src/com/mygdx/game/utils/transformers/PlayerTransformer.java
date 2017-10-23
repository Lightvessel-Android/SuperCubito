package com.mygdx.game.utils.transformers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.mygdx.game.World;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TagComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.enums.TagEntity;
import com.mygdx.game.utils.Assets;

public class PlayerTransformer extends EntityTransformer {

    private World world;

    public PlayerTransformer(PooledEngine engine, World world, EntityTransformer next) {
        super(engine, next);
        this.world = world;
        color = 279280639;
    }

    @Override
    public void createEntity(float posX, float posY) {

        Entity entity = engine.createEntity();

        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TagComponent tag = engine.createComponent(TagComponent.class);

        texture.region = Assets.player;

        tag.tag = TagEntity.PLAYER;

        bounds.bounds.width = PlayerComponent.WIDTH;
        bounds.bounds.height = PlayerComponent.HEIGHT;

        position.pos.set(posX, posY, 0.0f);

        state.set(PlayerComponent.STATE_ALIVE);

        entity.add(tag);
        entity.add(player);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(state);
        entity.add(texture);

        engine.addEntity(entity);
        world.setPlayer(entity);
    }
}
