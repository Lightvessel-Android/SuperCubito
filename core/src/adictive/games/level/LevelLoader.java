package adictive.games.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import adictive.games.SquareWorld;
import adictive.games.components.CameraComponent;
import adictive.games.components.MovementComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TextureComponent;
import adictive.games.components.TransformComponent;

public class LevelLoader {

    public static void load(SquareWorld world, Engine engine) {
        loadPlayer(world, engine);
        loadBlock(world, engine, 4, 4);
    }

    public static void loadPlayer(SquareWorld world, Engine engine) {
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
        player.add(transformComponent);

        player.add(new MovementComponent());

        engine.addEntity(player);
    }

    public static void loadBlock(SquareWorld world, Engine engine, int x, int y) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = new TextureRegion(new Texture(Gdx.files.internal("data/blackBox.png")));
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(1f, 1f);
        transformComponent.pos.set(x,y,0f);
        block.add(transformComponent);

        engine.addEntity(block);
    }
}
