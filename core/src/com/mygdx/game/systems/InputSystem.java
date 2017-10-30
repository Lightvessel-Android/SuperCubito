package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TransformComponent;

public class InputSystem extends EntitySystem {

    private Engine engine;

    private Vector3 touch;

    Vector2 playerPos;
    private CameraSystem cameraSystem;

    private Entity player;

    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<TransformComponent> tr;

    public InputSystem() {
        mm = ComponentMapper.getFor(MovementComponent.class);
        tr = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        cameraSystem = engine.getSystem(CameraSystem.class);

        touch = new Vector3(0, 0, 0);
        playerPos = new Vector2(0, 0);
    }

    @Override
    public void update(float deltaTime) {
        player = engine.getEntitiesFor(Family.all(PlayerComponent.class, BoundsComponent.class, MovementComponent.class).get()).first();

        MovementComponent mov = mm.get(player);

        TransformComponent pos = tr.get(player);

//        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
//            cameraSystem.addZoom(0.1f);
//        }
//
//        if (Gdx.input.isKeyPressed(Input.Keys.N)) {
//            cameraSystem.addZoom(-0.1f);
//        }
//
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
//            DebugSystem debugSystem = getEngine().getSystem(DebugSystem.class);
//            debugSystem.activated = !debugSystem.activated;
//        }

        if(Gdx.input.isTouched()) {

            cameraSystem.unproject(touch.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            playerPos.set(pos.pos.x, pos.pos.y);

            touch.sub(playerPos.x, playerPos.y, 0).nor();

            mov.velocity.set(touch.x, touch.y).scl(PlayerComponent.MOVE_VELOCITY);
        } else {
            mov.velocity.set(0, 0);
        }
    }


}
