package com.mygdx.game.utils.CollisionStructure;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public interface CollisionStructure {

    void clear();

    void insert(Entity entity);

    void update(Entity entity);

    Array<Entity> retrieve(Array<Entity> entitiesToReturn, Entity entity);

    int totalCount();

    void render(ShapeRenderer shapeRenderer);
}
