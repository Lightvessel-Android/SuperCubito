package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {
    public final Vector3 pos = new Vector3();
    public Vector2 scale = new Vector2(1f, 1f);
    public float rotation = 0.0f;
    public final Vector2 nextPosition = new Vector2(0, 0);
}