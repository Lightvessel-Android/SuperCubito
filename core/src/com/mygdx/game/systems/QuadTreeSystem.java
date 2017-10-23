package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.utils.QuadTree.QuadRectangle;
import com.mygdx.game.utils.QuadTree.QuadTree;

public class QuadTreeSystem extends EntitySystem {


    private Engine engine;
    private QuadTree quadTree;
    private ShapeRenderer shapeRenderer;
    private QuadRectangle rectangleAux;

    public QuadTreeSystem(float width,float height){
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.setAutoShapeType(true);
        quadTree = new QuadTree(new QuadRectangle(0,0, width, height), 0);
        rectangleAux = new QuadRectangle(0, 0, 0 ,0);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
            setupRectangle(entity);
            quadTree.insert(rectangleAux, entity);
        }
    }

    @Override
    public void update(float deltaTime) {

        setupWithQuadTree();
        quadTree = new QuadTree(rectangleAux, 0);

        for (Entity entity : engine.getEntitiesFor(Family.all(BoundsComponent.class).get())) {
                setupRectangle(entity);
                quadTree.insert(rectangleAux, entity);
        }
    }

    private void setupWithQuadTree() {
        rectangleAux.height = quadTree.zone.height;
        rectangleAux.width = quadTree.zone.width;
        rectangleAux.y = 0;
        rectangleAux.x = 0;
    }
    
    private void setupRectangle(Entity entity) {
        Rectangle bounds = entity.getComponent(BoundsComponent.class).bounds;
        rectangleAux.x = bounds.getX();
        rectangleAux.y = bounds.getY();
        rectangleAux.height = bounds.getHeight();
        rectangleAux.width = bounds.getWidth();
    }

    public QuadTree getQuadTree(){
        return quadTree;
    }



}
