package adictive.games.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import adictive.games.SquareWorld;
import adictive.games.components.BoundsComponent;
import adictive.games.components.CameraComponent;
import adictive.games.components.EnemyComponent;
import adictive.games.components.MovementComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TextureComponent;
import adictive.games.components.TransformComponent;
import adictive.games.components.WallComponent;

public class LevelLoader {

    private final String fileName;
    private SquareWorld world;
    private Engine engine;

    public LevelLoader(String fileName, SquareWorld world, Engine engine) {
        this.fileName = fileName;
        this.world = world;
        this.engine = engine;
    }

    public void load() {
        loadPlayer(world, engine, world.getWidth()/2, world.getHeight()/2);

        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader("levels/" + fileName));
            while ((line = br.readLine()) != null) {

                String[] entity = line.split(",");
                if (entity[0].equals("Block")) {
                    parseBlock(entity);
                } else if (entity[0].equals("Enemy")) {
                    parseEnemy(entity);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseEnemy(String[] line) {
        EnemyComponent.createEnemy(
                engine, 0,0,
                Float.parseFloat(line[1]),
                Float.parseFloat(line[2]),
                Float.parseFloat(line[3]),
                Float.parseFloat(line[4]),
                Float.parseFloat(line[5])
        );
    }

    public void parseBlock(String[] line) {
        loadBlock(world,engine,Integer.parseInt(line[1]),Integer.parseInt(line[2]));
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
