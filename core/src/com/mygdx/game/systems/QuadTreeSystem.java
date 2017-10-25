package com.mygdx.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.utils.QuadTree.QuadTree;

public class QuadTreeSystem extends EntitySystem {

    QuadTree quadTree;
    private final Family boundables = Family.all(BoundsComponent.class).get();

    public QuadTreeSystem(){
        quadTree = new QuadTree(1, new Rectangle(0, 0, 64, 64));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        quadTree.clear();
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(boundables);
        for (int i = 0; i < entities.size(); i++) {
            quadTree.insert(entities.get(i));
        }
    }
}
