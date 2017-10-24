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

    public QuadTreeSystem(float width,float height){
        quadTree = new QuadTree(new Rectangle(0, 0, width ,height), 0);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
            quadTree.insert(entity.getComponent(BoundsComponent.class).bounds, entity);
        }
    }

    @Override
    public void update(float deltaTime) {

        quadTree = new QuadTree(quadTree.getZone(), 0);

        for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
                //System.out.println("TAG : " + entity.getComponent(TagComponent.class).tag);
                //System.out.println("BOUND : " + entity.getComponent(BoundsComponent.class).bounds);

                quadTree.insert(entity.getComponent(BoundsComponent.class).bounds, entity);
        }
    }

    public QuadTree getQuadTree(){
        return quadTree;
    }

}
