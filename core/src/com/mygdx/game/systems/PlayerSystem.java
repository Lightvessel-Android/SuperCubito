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

    private float accelX = 0.0f;
    private World world;

    private ComponentMapper<PlayerComponent> pm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

    public PlayerSystem(World world) {
        super(family);

        this.world = world;

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
        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);
        PlayerComponent player = pm.get(entity);

//        if (state.get() != BobComponent.STATE_HIT && t.pos.y <= 0.5f) {
//            hitPlatform(entity);
//        }
//
//        if (state.get() != BobComponent.STATE_HIT) {
//            mov.velocity.x = -accelX / 10.0f * BobComponent.MOVE_VELOCITY;
//        }
//
//        if (mov.velocity.y > 0 && state.get() != BobComponent.STATE_HIT) {
//            if (state.get() != BobComponent.STATE_JUMP) {
//                state.set(BobComponent.STATE_JUMP);
//            }
//        }
//
//        if (mov.velocity.y < 0 && state.get() != BobComponent.STATE_HIT) {
//            if (state.get() != BobComponent.STATE_FALL) {
//                state.set(BobComponent.STATE_FALL);
//            }
//        }

        if (t.pos.x < 0) {
            t.pos.x = World.WORLD_WIDTH;
        }

        if (t.pos.x > World.WORLD_WIDTH) {
            t.pos.x = 0;
        }

        t.scale.x = mov.velocity.x < 0.0f ? Math.abs(t.scale.x) * -1.0f : Math.abs(t.scale.x);

        player.heightSoFar = Math.max(t.pos.y, player.heightSoFar);

        if (player.heightSoFar - 7.5f > t.pos.y) {
            world.state = World.WORLD_STATE_GAME_OVER;
        }
    }

    public void dead (Entity entity) {
        if (!family.matches(entity)) return;

        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);

        mov.velocity.set(0, 0);
        state.set(PlayerComponent.STATE_DEAD);
    }
//
//    public void hitPlatform (Entity entity) {
//        if (!family.matches(entity)) return;
//
//        StateComponent state = sm.get(entity);
//        MovementComponent mov = mm.get(entity);
//
//        mov.velocity.y = BobComponent.JUMP_VELOCITY;
//        state.set(BobComponent.STATE_JUMP);
//    }
//
//    public void hitSpring (Entity entity) {
//        if (!family.matches(entity)) return;
//
//        StateComponent state = sm.get(entity);
//        MovementComponent mov = mm.get(entity);
//
//        mov.velocity.y = BobComponent.JUMP_VELOCITY * 1.5f;
//        state.set(BobComponent.STATE_JUMP);
//    }
}