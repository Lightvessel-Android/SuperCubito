package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class PlayerComponent implements Component {

    public static final int STATE_ALIVE = 1;
    public static final int STATE_DEAD = 2;
    public static final int STATE_HIT = 3;

    public static final float MOVE_VELOCITY = 10;
    public static final float WIDTH = 0.8f;
    public static final float HEIGHT = 0.8f;
}