package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;

import java.util.Comparator;
import java.util.PriorityQueue;

public class RenderingSystem extends EntitySystem {
    public static final float FRUSTUM_SIDE = 15;
    public static final float FRUSTUM_HEIGHT = 15;
    public static final float PIXELS_TO_METERS = 1.0f / 32.0f;
    public static final float CELL_TO_METERS = 1.0f;

    private SpriteBatch batch;
    private OrthographicCamera cam;

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    private PriorityQueue<Entity> renderQueue;

    public RenderingSystem(SpriteBatch batch) {
        super();

        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);

        renderQueue = new PriorityQueue<Entity>(512, new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int)Math.signum(transformM.get(entityB).pos.z -
                        transformM.get(entityA).pos.z);
            }
        });

        this.batch = batch;

        cam = new OrthographicCamera();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        cam.position.set(FRUSTUM_SIDE / 2, FRUSTUM_HEIGHT / 2, 0);

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(
                Family.all(TransformComponent.class, TextureComponent.class).get(),
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
        super.update(deltaTime);

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        for (Entity entity : renderQueue) {
            renderEntity(entity);
        }

        batch.end();
    }

    private void renderEntity(Entity entity) {
        TextureComponent tex = textureM.get(entity);

        if (tex.region == null) {
            return;
        }

        TransformComponent t = transformM.get(entity);

        float width = tex.region.getRegionWidth();
        float height = tex.region.getRegionHeight();
        float originX = width * 0.5f;
        float originY = height * 0.5f;

        batch.draw(tex.region,
            t.pos.x - originX, t.pos.y - originY,
            originX, originY,
            width, height,
            t.scale.x * PIXELS_TO_METERS, t.scale.y * PIXELS_TO_METERS,
            MathUtils.radiansToDegrees * t.rotation);
    }

    public void resize(int deviceWidth, int deviceHeight) {
        resize(cam, deviceWidth, deviceHeight);
    }

    public static void resize(OrthographicCamera cam, int deviceWidth, int deviceHeight) {

        float width =  deviceWidth / Gdx.graphics.getDensity();
        float height = deviceHeight / Gdx.graphics.getDensity();

        if (width < height) {
            cam.viewportWidth = FRUSTUM_SIDE;
            cam.viewportHeight = FRUSTUM_SIDE * ((height)/width);
            cam.zoom = 480f / height;
        } else {
            cam.viewportWidth = FRUSTUM_SIDE * ((width)/height);
            cam.viewportHeight = FRUSTUM_SIDE;
            cam.zoom = 640f / width;
        }
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}