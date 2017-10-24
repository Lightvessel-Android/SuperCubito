package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.utils.QuadTree.QuadTreeV2.QuadTreeNode;

public class QuadTreeSystem extends EntitySystem {


    private Engine engine;
    private QuadTreeNode quadTree;

    public QuadTreeSystem(float width,float height){
        quadTree = new QuadTreeNode(0, new Rectangle(0, 0, width ,height));
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        quadTree.clear();
        addAll(engine);
    }

    private void addAll(Engine engine) {
        for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
            quadTree.insert(entity);
        }
    }

    public QuadTreeNode getQuadTree(){
        return quadTree;
    }

}
