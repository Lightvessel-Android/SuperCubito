package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import adictive.games.SquareWorld;

public class WallComponent implements Component {
    public static Entity createBlock(SquareWorld world, Engine engine, int x, int y) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = new TextureRegion(new Texture(Gdx.files.internal("data/blackBox.png")));
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(1f, 1f);
        transformComponent.pos.set(x,y,0f);
        block.add(transformComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,1f,1f);
        block.add(boundsComponent);

        block.add(new WallComponent());

        engine.addEntity(block);
        return block;
    }
}
