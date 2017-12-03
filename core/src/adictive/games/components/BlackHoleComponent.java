package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BlackHoleComponent implements Component {
    public static final float SIDE = 1.5f;
    public static final TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/blackHole.png")));
    public float attraction;

    public static Entity addNew(Engine engine, float x, float y, float attraction) {
        Entity block = create(x, y, attraction);
        engine.addEntity(block);
        return block;
    }

    public static Entity create(float x, float y, float attraction) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = textureRegion;
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(SIDE, SIDE);
        transformComponent.pos.set(x,y,0f);
        block.add(transformComponent);

        BlackHoleComponent enemyComponent = new BlackHoleComponent();
        enemyComponent.attraction = attraction;
        block.add(enemyComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0f,0f,1f,1f);
        block.add(boundsComponent);

        LightComponent lightComponent = new LightComponent();
        lightComponent.color = new Color(0.05f,0.05f,0.05f,0.8f);
        lightComponent.radius = 4f;
        block.add(lightComponent);

        return block;
    }
}
