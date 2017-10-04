package com.mygdx.game.utils.transformers;

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
        }
    }

    public abstract void createEntity(float posX, float posY);
}
