package adictive.games.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import adictive.games.SquareWorld;
import adictive.games.components.BoundsComponent;
import adictive.games.components.CameraComponent;
import adictive.games.components.MovementComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TextureComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;

public class LevelLoader {

    public static void load(SquareWorld world, Engine engine) {
        loadPlayer(world, engine, world.getWidth()/2, world.getHeight()/2);

        for (int x = 0; x < world.getWidth();x++) {
            loadBlock(world, engine, x, 0);
            loadBlock(world, engine, x, world.getHeight() - 1);
        }

        for (int y = 1; y < world.getHeight() - 1;y++) {
            loadBlock(world, engine, 0, y);
            loadBlock(world, engine, world.getWidth() - 1, y);
        }
    }

    public static void loadPlayer(SquareWorld world, Engine engine, int x, int y) {
        Entity player = new Entity();

        player.add(new PlayerComponent());

        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.camera = world.getCamera();
        player.add(cameraComponent);

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = new TextureRegion(new Texture(Gdx.files.internal("data/player.png")));
        player.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(0.5f, 0.5f);
        transformComponent.pos.set(x,y,0f);
        player.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,0.5f,0.5f);
        player.add(boundsComponent);

        player.add(new MovementComponent());

        engine.addEntity(player);
    }

    public static void loadBlock(SquareWorld world, Engine engine, int x, int y) {
        WallComponent.createBlock(world, engine, x, y);
    }
}
