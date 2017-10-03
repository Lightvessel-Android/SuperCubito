package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.World;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TransformComponent;

public class PlayerSystem extends IteratingSystem {
    private static final Family family = Family.all(PlayerComponent.class,
            StateComponent.class,
            TransformComponent.class,
            MovementComponent.class).get();

    private World world;

    private ComponentMapper<PlayerComponent> pm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

    public PlayerSystem(World worldD) {
        super(family);

        world = worldD;

        pm = ComponentMapper.getFor(PlayerComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
    }

    public void dead (Entity entity) {
        if (!family.matches(entity)) return;

        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);

        mov.velocity.set(0, 0);

        state.set(PlayerComponent.STATE_DEAD);
    }

    public void hitBlock(Entity entity) {
        StateComponent state = sm.get(entity);
        TransformComponent tr = tm.get(entity);

        state.set(PlayerComponent.STATE_HIT);
        tr.pos.set(tr.lastPosition, 0);
    }
}