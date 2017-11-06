package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CoinComponent implements Component {

    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 0.5f;

    private static final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/coin.png")));

    public static Entity createCoin(Engine engine, float x, float y) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = textureRegion;
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(WIDTH, HEIGHT);
        transformComponent.pos.set(x - transformComponent.size.x/2,y - transformComponent.size.y/2,0f);
        block.add(transformComponent);

        block.add(new CoinComponent());

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,WIDTH,HEIGHT);
        block.add(boundsComponent);

        engine.addEntity(block);
        return block;
    }
}
