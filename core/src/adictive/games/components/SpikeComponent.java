package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpikeComponent implements Component {
    public static final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/spikes.png")));

    public static Entity create(int x, int y, float rotation) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = textureRegion;
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(1f, 1f);
        transformComponent.pos.set(x, y,0f);
        transformComponent.rotation = rotation;
        block.add(transformComponent);

        block.add(new SpikeComponent());

        BoundsComponent boundsComponent = new BoundsComponent();
        switch ((int) rotation) {
            case 0:boundsComponent.bounds.set(0,0,0.5f,1f);break;
            case 90:boundsComponent.bounds.set(0,0,1f,0.5f);break;
            case 180:boundsComponent.bounds.set(0.5f,0,0.5f,1f);break;
            case 270:boundsComponent.bounds.set(0,0.5f,1f,0.5f);break;
        }
        block.add(boundsComponent);

        return block;
    }

    public static void addNew(Engine engine, int x, int y, float rotation) {
        engine.addEntity(create(x, y, rotation));
    }
}
