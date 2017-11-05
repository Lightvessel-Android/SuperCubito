package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WinComponent implements Component {

    private static final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/winBox.png")));
    public static final float WIDTH = 1f;
    public static final float HEIGHT = 1f;

    public static Entity createWinComponent(Engine engine, float x, float y) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();

        textureComponent.region = textureRegion;
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(WIDTH, HEIGHT);
        transformComponent.pos.set((int)x,(int)y,99f);
        block.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,WIDTH,HEIGHT);
        block.add(boundsComponent);

        block.add(new WinComponent());

        engine.addEntity(block);
        return block;
    }

}
