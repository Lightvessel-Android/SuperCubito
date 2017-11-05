package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Comparator;
import java.util.PriorityQueue;

import adictive.games.SquareWorld;
import adictive.games.components.TextureComponent;
import adictive.games.components.TransformComponent;

public class RenderingSystem extends EntitySystem {

    public static int VIEWPORT_WIDTH_MTS = 15;
    public static int VIEWPORT_HEIGHT_MTS = 15;
    public static final Family FAMILY = Family.all(TextureComponent.class, TransformComponent.class).get();

    private final PriorityQueue<Entity> renderQueue;

    private final ComponentMapper<TransformComponent> transformMapper;
    private final ComponentMapper<TextureComponent> textureMapper;
    private final SquareWorld world;
    private final SpriteBatch batch;



    public RenderingSystem(SquareWorld world, SpriteBatch batch) {
        super(10);
        this.world = world;
        this.batch = batch;
        textureMapper = ComponentMapper.getFor(TextureComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);

        renderQueue = new PriorityQueue<>(16, new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int) Math.signum(transformMapper.get(entityB).pos.z -
                        transformMapper.get(entityA).pos.z);
            }
        });
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        resize(world.getCamera(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        engine.addEntityListener(
                FAMILY,
                new EntityListener() {
                    @Override
                    public void entityAdded(Entity entity) {
                        renderQueue.add(entity);
                    }

                    @Override
                    public void entityRemoved(Entity entity) {
                        renderQueue.remove(entity);
                    }
                }
        );
    }

    @Override
    public void update(float deltaTime) {
        world.getCamera().update();
        batch.setProjectionMatrix(world.getCamera().combined);
        batch.begin();
        for (Entity entity : renderQueue) {
            renderEntity(entity);
        }
        batch.end();
    }

    private void renderEntity(Entity entity) {
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
