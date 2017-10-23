package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.utils.QuadTree.QuadTree;

public class QuadTreeSystem extends EntitySystem {


    private Engine engine;
    private QuadTree quadTree;

    public QuadTreeSystem(){
        quadTree = new QuadTree(1, new Rectangle(0, 0, 64, 64));
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;

        for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
            quadTree.insert(entity);
        }
    }

    @Override
    public void update(float deltaTime) {
        // habria que ver como solo sacar y volver a agregar las entidades que se movieron
       quadTree.clear();
       for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
            quadTree.insert(entity);
        }
    }

    public QuadTree getQuadTree(){
        return quadTree;
    }



}
