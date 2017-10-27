package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Pixmap;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.utils.CollisionStructure.CollisionStructure;
import com.mygdx.game.utils.CollisionStructure.Grid;

public class CollisionStructureSystem extends EntitySystem {

    CollisionStructure collisionStructure;
    private final Family boundables = Family.all(BoundsComponent.class).get();
    private final Family movebles = Family.all(MovementComponent.class).get();

    public CollisionStructureSystem(Pixmap pixmap){
        collisionStructure = new Grid(pixmap.getWidth(),pixmap.getHeight(), 1);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        collisionStructure.clear();

        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(boundables);
        for (int i = 0; i < entities.size(); i++) {
            collisionStructure.insert(entities.get(i));
        }
    }
}
