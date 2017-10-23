package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.utils.QuadTree.QuadTree;

public class QuadTreeSystem extends EntitySystem {


    private Engine engine;
    private QuadTree quadTree;
    private ShapeRenderer shapeRenderer;

    public QuadTreeSystem(float width,float height){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.setAutoShapeType(true);
        quadTree = new QuadTree(0, new Rectangle(0,0, width, height));
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
//        shapeRenderer.begin();
//        quadTree.draw(shapeRenderer);
//        shapeRenderer.end();

        quadTree.clear();
        for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
                quadTree.insert(entity);
        }
    }

    public QuadTree getQuadTree(){
        return quadTree;
    }



}
