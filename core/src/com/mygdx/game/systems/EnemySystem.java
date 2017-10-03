package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.EnemyComponent;
import com.mygdx.game.components.MovementComponent;
import com.mygdx.game.components.TransformComponent;

public class EnemySystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;

    private Engine engine;

    public EnemySystem() {
        super(Family.all(EnemyComponent.class,
                TransformComponent.class,
                MovementComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        MovementComponent mov = mm.get(entity);

        t.scale.x = mov.velocity.x < 0.0f ? Math.abs(t.scale.x) * -1.0f : Math.abs(t.scale.x);
    }

    @Override
    public void update(float deltaTime) {
        CollisionSystem collisionSystem = engine.getSystem(CollisionSystem.class);

        for (Entity entity : collisionSystem.getEnemiesCol()){
            MovementComponent enemyMov = mm.get(entity);

            enemyMov.velocity.scl(-1);
        }
    }
}
