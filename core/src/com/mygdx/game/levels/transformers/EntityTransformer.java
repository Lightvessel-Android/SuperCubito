package com.mygdx.game.levels.transformers;

import com.badlogic.ashley.core.PooledEngine;

public abstract class EntityTransformer {

    protected PooledEngine engine;
    protected float color;

    private EntityTransformer next;

    public EntityTransformer(PooledEngine engine, EntityTransformer next){
        this.next = next;
        this.engine = engine;
    }

    public void create(float posX, float posY, float pixel){
        if(pixel == color){
            createEntity(posX, posY);
        } else if (next != null){
            next.create(posX, posY, pixel);
        } else {
            System.out.println("Unknown pixel: " + pixel);
        }
    }

    public abstract void createEntity(float posX, float posY);
}
