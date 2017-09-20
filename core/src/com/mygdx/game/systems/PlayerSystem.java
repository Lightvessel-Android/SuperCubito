package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
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

    private float accelX = 0.0f;
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

    public void setAccelX(float accelX) {
        this.accelX = accelX;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        accelX = 0.0f;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        MovementComponent mov = mm.get(entity);
        StateComponent state = sm.get(entity);

        if (state.get() == PlayerComponent.STATE_HIT) {
            mov.velocity.scl(-1);
            state.set(PlayerComponent.STATE_ALIVE);
        }
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
        state.set(PlayerComponent.STATE_HIT);
    }
}