package com.mygdx.game.utils.QuadTree;

import com.badlogic.gdx.math.Rectangle;

public class QuadNode<T> {
    Rectangle r;
    T element;

    QuadNode(Rectangle r, T element) {
        this.r = r;
        this.element = element;
    }

    @Override
    public String toString() {
        return r.toString();
    }
}