package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;

public class InputSystem extends EntitySystem {

    private Engine engine;

    private Vector2 velocity;

    private Entity player;

    private ComponentMapper<MovementComponent> mm;

    public InputSystem() {
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        velocity = new Vector2(0, 0);
    }

    @Override
    public void update(float deltaTime) {
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class, BoundsComponent.class, MovementComponent.class).get()).first();

        MovementComponent mov = mm.get(player);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            velocity.set(- PlayerComponent.MOVE_VELOCITY, 0);
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            velocity.set(PlayerComponent.MOVE_VELOCITY, 0);
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            velocity.set(0, -PlayerComponent.MOVE_VELOCITY);
        else if (Gdx.input.isKeyPressed(Input.Keys.UP))
            velocity.set(0, PlayerComponent.MOVE_VELOCITY);
        else
            velocity.set(0, 0);

        mov.velocity.set(velocity);
    }


}
