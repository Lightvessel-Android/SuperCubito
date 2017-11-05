package adictive.games.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import adictive.games.SquareWorld;

public class EnemyComponent implements Component {
    private static TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal("data/enemy.png")));
    public final Vector2 direction = new Vector2(3,0);
    public float vel = 5f;
    public float posInLine = 0f;
    public final Vector2 start = new Vector2();
    public final Vector2 end = new Vector2();

    public void resetDirection() {
        direction.set(end.x - start.x, end.y - start.y);
    }

    public static Entity createEnemy(SquareWorld world, Engine engine, float x, float y) {
        Entity block = new Entity();

        TextureComponent textureComponent = new TextureComponent();
        textureComponent.region = textureRegion;
        block.add(textureComponent);

        TransformComponent transformComponent = new TransformComponent();
        transformComponent.size.set(0.5f, 0.5f);
        transformComponent.pos.set(x - transformComponent.size.x/2,y - transformComponent.size.y/2,0f);
        block.add(transformComponent);

        EnemyComponent enemyComponent = new EnemyComponent();
        block.add(enemyComponent);

        BoundsComponent boundsComponent = new BoundsComponent();
        boundsComponent.bounds.set(0,0,0.5f,0.5f);
        block.add(boundsComponent);

        engine.addEntity(block);
        return block;
    }
}
