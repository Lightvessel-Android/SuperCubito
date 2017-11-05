package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import adictive.games.SquareWorld;

public class PlayerComponent implements Component {
    public static final float MOVE_VELOCITY = 8;
    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 0.5f;

    public static void createPlayer(SquareWorld world, Engine engine, float x, float y) {
        Entity player = new Entity();

        player.add(new PlayerComponent());

        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.camera = world.getCamera();
        player.add(cameraComponent);

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = new TextureRegion(new Texture(Gdx.files.internal("data/player.png")));
        player.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(WIDTH, HEIGHT);
        transformComponent.pos.set(x - transformComponent.size.x/2,y - transformComponent.size.y/2,0f);
        player.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,0.5f,0.5f);
        player.add(boundsComponent);

        player.add(new MovementComponent());

        engine.addEntity(player);
    }
}
