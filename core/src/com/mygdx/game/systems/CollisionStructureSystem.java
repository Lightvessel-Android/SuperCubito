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

    private Engine engine;
    CollisionStructure collisionStructure;
    private final Family boundables = Family.all(BoundsComponent.class).get();
    private final Family movebles = Family.all(MovementComponent.class).get();

    public CollisionStructureSystem(Pixmap pixmap){
        collisionStructure = new Grid(pixmap.getWidth(),pixmap.getHeight(), 3);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(boundables);
        for (int i = 0; i < entities.size(); i++) {
            collisionStructure.insert(entities.get(i));
        }
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(movebles);
        for (int i = 0; i < entities.size(); i++) {
            collisionStructure.update(entities.get(i));
        }
    }
}
