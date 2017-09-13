package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {

    public static final float MOVE_VELOCITY = 20;
    public static final float WIDTH = 0.8f;
    public static final float HEIGHT = 0.8f;

    public float heightSoFar = 0.0f;
}