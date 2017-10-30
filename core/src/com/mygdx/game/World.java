package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.components.BackgroundComponent;
import com.mygdx.game.components.CameraComponent;
import com.mygdx.game.components.TagComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.enums.TagEntity;
import com.mygdx.game.levels.LevelLoader;
import com.mygdx.game.enums.WorldState;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.enums.WorldState.WORLD_STATE_RUNNING;
import static com.mygdx.game.systems.RenderingSystem.CELL_TO_METERS;
import static com.mygdx.game.systems.RenderingSystem.PIXELS_TO_METERS;

public class World {
    public WorldState state;

    private PooledEngine engine;

    private LevelLoader levelLoader;

    private Entity player;

    private int width;
    private int height;

    public World (PooledEngine engine, Pixmap pixmap) {
        this.engine = engine;
        levelLoader = new LevelLoader(pixmap, engine, this);
        width = pixmap.getWidth();
        height = pixmap.getHeight();
    }

    public void create() {
        createBackground();
        levelLoader.generateLevel();

        createCamera(player);

        state = WORLD_STATE_RUNNING;
    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraComponent camera = new CameraComponent();
        TagComponent tag = engine.createComponent(TagComponent.class);
        camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
        camera.target = target;
        tag.tag = TagEntity.CAMERA;

        entity.add(tag);
        entity.add(camera);

        engine.addEntity(entity);
    }

    private void createBackground() {
        Entity entity = engine.createEntity();

        BackgroundComponent background = engine.createComponent(BackgroundComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TagComponent tag = engine.createComponent(TagComponent.class);

        texture.region = Assets.background;

        tag.tag = TagEntity.BACKGROUND;

        texture.region.setRegionWidth((int) (width / PIXELS_TO_METERS) * 2);
        texture.region.setRegionHeight((int) (height  / PIXELS_TO_METERS) * 2);
        position.pos.set(width /2, height/2, 10 );


        entity.add(tag);
        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    public void setPlayer(Entity player) {
        this.player = player;
    }
}
