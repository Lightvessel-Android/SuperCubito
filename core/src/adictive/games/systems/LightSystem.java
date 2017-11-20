package adictive.games.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;

import adictive.games.SquareWorld;
import adictive.games.components.LightComponent;
import adictive.games.components.TransformComponent;

public class LightSystem extends EntitySystem {

    public static final Family LIGHTS = Family.all(LightComponent.class).get();

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<LightComponent> lightMapper = ComponentMapper.getFor(LightComponent.class);
    private final SquareWorld world;
    private final SpriteBatch lightBatch = new SpriteBatch();;
    private final Matrix4 screenProjection = new Matrix4();

    private FrameBuffer lightBuffer;
    private TextureRegion lightBufferRegion;

    public LightSystem(SquareWorld world) {
        super(10);
        this.world = world;

        float lowDisplayW = Gdx.graphics.getWidth();
        float lowDisplayH = Gdx.graphics.getHeight();

        lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, powerOfTwo(lowDisplayW), powerOfTwo(lowDisplayH), false);
        lightBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture());
        lightBufferRegion.flip(false, true);
    }

    @Override
    public void update(float deltaTime) {
        renderLights();
    }

    public void resize(int w, int h) {
        screenProjection.setToOrtho2D(0, 0, w, h);
    }

    private void renderLights() {
        lightBuffer.begin();
        Gdx.gl.glClearColor(0xFF/255f, 0xFC/255f, 0xEF/255f, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ZERO);
        lightBatch.enableBlending();
        lightBatch.setProjectionMatrix(world.getCamera().combined);
        lightBatch.begin();

        ImmutableArray<Entity> lights = getEngine().getEntitiesFor(LIGHTS);
        for (Entity entity : lights) {
            renderLightOn(entity);
        }

        lightBatch.end();
        lightBuffer.end();

        Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        lightBatch.setProjectionMatrix(screenProjection);
        lightBatch.begin();
        lightBatch.setColor(Color.WHITE);
        lightBatch.draw(lightBufferRegion,0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        lightBatch.end();
    }

    private void renderLightOn(Entity entity) {
        final TransformComponent transformComponent = transformMapper.get(entity);
        final LightComponent lightComponent = lightMapper.get(entity);
        final float lightSize = lightComponent.radius;
        lightBatch.setColor(lightComponent.color);

        this.lightBatch.draw(
                lightComponent.shape,
                transformComponent.pos.x + transformComponent.size.x/2 - lightSize/2,
                transformComponent.pos.y + transformComponent.size.y/2 - lightSize/2,
                lightSize,lightSize
        );
    }

    private static int powerOfTwo(float n) {
        return (int) Math.pow(2, Math.ceil(Math.log(n)/Math.log(2)));
    }
}
