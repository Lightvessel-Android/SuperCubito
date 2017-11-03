package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import adictive.games.SquareWorld;
import adictive.games.components.TextureComponent;
import adictive.games.components.TransformComponent;

public class RenderingSystem extends IteratingSystem {

    public static int VIEWPORT_WIDTH_MTS = 15;
    public static int VIEWPORT_HEIGHT_MTS = 15;

    private final ComponentMapper<TransformComponent> transformMapper;
    private final ComponentMapper<TextureComponent> textureMapper;
    private final SquareWorld world;
    private final SpriteBatch batch;

    public RenderingSystem(SquareWorld world, SpriteBatch batch) {
        super(Family.all(TextureComponent.class, TransformComponent.class).get(), 10);
        this.world = world;
        this.batch = batch;
        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        resize(world.getCamera(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void update(float deltaTime) {
        world.getCamera().update();
        batch.setProjectionMatrix(world.getCamera().combined);
        batch.begin();
        super.update(deltaTime);
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final TextureComponent textureComponent = textureMapper.get(entity);
        final TransformComponent transformComponent = transformMapper.get(entity);

        this.batch.draw(
            textureComponent.region,
            transformComponent.pos.x,
            transformComponent.pos.y,
            transformComponent.size.x,
            transformComponent.size.y
        );
    }

    public void resize(int w, int h) {
        resize(world.getCamera(), w, h);
    }

    public static void resize(OrthographicCamera cam, int deviceWidth, int deviceHeight) {

        float width =  deviceWidth / Gdx.graphics.getDensity();
        float height = deviceHeight / Gdx.graphics.getDensity();

        if (width < height) {
            cam.viewportWidth = VIEWPORT_WIDTH_MTS;
            cam.viewportHeight = VIEWPORT_HEIGHT_MTS * ((height)/width);
            cam.zoom = 480f / height;
        } else {
            cam.viewportWidth = VIEWPORT_WIDTH_MTS * ((width)/height);
            cam.viewportHeight = VIEWPORT_HEIGHT_MTS;
            cam.zoom = 640f / width;
        }
    }
}
