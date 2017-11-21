package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CoinComponent implements Component {

    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 0.5f;

    public static final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/coin.png")));

    public static Entity addNew(Engine engine, float x, float y) {
        Entity block = create(x, y);

        engine.addEntity(block);
        return block;
    }

    public static Entity create(float x, float y) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = textureRegion;
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(WIDTH, HEIGHT);
        transformComponent.pos.set(x,y,0f);
        block.add(transformComponent);

        block.add(new CoinComponent());

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,WIDTH,HEIGHT);
        block.add(boundsComponent);

        LightComponent lightComponent = new LightComponent();
        lightComponent.color = new Color(1f,1f,0f,0.4f);
        block.add(lightComponent);
        return block;
    }
}
