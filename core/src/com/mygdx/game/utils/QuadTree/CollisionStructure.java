package com.mygdx.game.utils.QuadTree;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public interface CollisionStructure {

    void clear();

    void insert(Entity entity);

    Array<Entity> retrieve(Array<Entity> entitiesToReturn, Entity entity);
}
