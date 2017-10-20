package com.mygdx.game.utils.QuadTree;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.BoundsComponent;

public class QuadTree {
    private static final int MAX_OBJECTS = 1;
    private static final int MAX_LEVELS = 3;

    private int level;
    private Rectangle bounds;
    private Array<Entity> entities;
    private QuadTree[] nodes;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;
        this.entities = new Array<Entity>();
        this.nodes = new QuadTree[4];
    }

    public void draw(ShapeRenderer shapes) {
        shapes.setColor(Color.MAGENTA);
        shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        for (int i = 0; i < nodes.length; i++) {
            QuadTree node = nodes[i];
            if (node != null) {
                node.draw(shapes);
            }
        }
    }

    public void clear() {
        entities.clear();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        final float x = bounds.x;
        final float y = bounds.y;
        final float w = bounds.width / 2f;
        final float h = bounds.height / 2f;
        final int level = this.level++;

        nodes[0] = new QuadTree(level, new Rectangle(x + w, y, w, h));
        nodes[1] = new QuadTree(level, new Rectangle(x, y, w, h));
        nodes[2] = new QuadTree(level, new Rectangle(x, y + h, w, h));
        nodes[3] = new QuadTree(level, new Rectangle(x + w, y + h, w, h));
    }

    private int getIndex(Rectangle r) {
        int index = -1;

        float verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2f);
        float horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2f);

        boolean topQuadrant = r.getY() < horizontalMidpoint && r.getY() + r.getHeight() < horizontalMidpoint;
        boolean bottomQuadrant = r.getY() > horizontalMidpoint;

        if (r.getX() < verticalMidpoint && r.getX() + r.getWidth() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            } else if (bottomQuadrant) {
                index = 2;
            }
        } else if (r.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            } else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    public void insert(Entity entity) {
        if (nodes[0] != null) {
            final int index = getIndex(entity.getComponent(BoundsComponent.class).bounds);
            if (index != -1) {
                nodes[index].insert(entity);
                return;
            }
        }

        entities.add(entity);

        if (entities.size > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < entities.size) {
                final int index = getIndex(entity.getComponent(BoundsComponent.class).bounds);
                if (index != -1) {
                    nodes[index].insert(entities.removeIndex(i));
                } else {
                    i++;
                }
            }
        }
    }

    public void retrieve(Array<Entity> entities, Rectangle r) {
        if (nodes[0] != null) {
            final int index = getIndex(r);
            if (index != -1) {
                nodes[index].retrieve(entities, r);
            } else {
                for (int i = 0; i < nodes.length; i++) {
                    nodes[i].retrieve(entities, r);
                }
            }
        }
        entities.addAll(this.entities);
    }
}