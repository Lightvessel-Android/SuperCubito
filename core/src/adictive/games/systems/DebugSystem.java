package adictive.games.systems;


import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import adictive.games.SquareWorld;

public class DebugSystem extends EntitySystem {

    private SquareWorld world;

    public DebugSystem(SquareWorld world) {
        this.world = world;
    }

    @Override
    public void update(float deltaTime) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            world.getCamera().zoom += 0.1f;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            world.getCamera().zoom -= 0.1f;
        }

    }
}
