package com.mygdx.game.utils.QuadTree.QuadTreeV2;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.BoundsComponent;


public class QuadTreeNode {
    // Max number to store in this quad before subdividing
    public static final int MAX_ENTITIES = 4;
    private static final int MAX_LEVELS = 5;

    // Spatial bounds of this node
    private Rectangle boundary;

    // All entities in this quad
    private Array<Entity> entities;

    // The level of this node
    private int level;

    // Child QuadTreeNodes
    private Array<QuadTreeNode> childNodes;

    // Scratch variables
    private static Vector2 BOUNDS_CENTER = null;

    public QuadTreeNode(int level, Rectangle boundary)
    {
        this.level = level;
        this.boundary = boundary;

        entities = new Array<>(true, MAX_ENTITIES);

        // NW, NE, SE, SW (Clockwise)
        childNodes = new Array<>(true, 4);

        if(BOUNDS_CENTER == null)
        {
            BOUNDS_CENTER = new Vector2();
        }
    }

    /**
     * Clear out all entities and null out all child nodes
     */
    public void clear()
    {
        // Clear the entities
        entities.clear();

        // Clear out each child node
        for(QuadTreeNode currNode : childNodes)
        {
            if(currNode != null)
            {
                currNode.clear();
                currNode = null;
            }
        }

        // Lastly, clear out the nodes list
        childNodes.clear();
    }

    /**
     * Subdivide the node into 4 child nodes
     */
    private void subdivide()
    {
        float width_div2 = boundary.width / 2;
        float height_div2 = boundary.height / 2;
        float x = boundary.x;
        float y = boundary.y;

        // Create four child node which fully divide the boundary of this node
        Rectangle nwRect = new Rectangle(x, y + height_div2, width_div2, height_div2);
        childNodes.add(new QuadTreeNode(level + 1, nwRect));

        Rectangle neRect = new Rectangle(x + width_div2, y + height_div2, width_div2, height_div2);
        childNodes.add(new QuadTreeNode(level + 1, neRect));

        Rectangle seRect = new Rectangle(x + width_div2, y, width_div2, height_div2);
        childNodes.add(new QuadTreeNode(level + 1, seRect));

        Rectangle swRect = new Rectangle(x, y, width_div2, height_div2);
        childNodes.add(new QuadTreeNode(level + 1, swRect));
    }

    /**
     * Determine which node the entity belongs to. -1 means object cannot completely fit within a child node
     * and is part of the parent node
     */
    private int getIndex(Entity entity)
    {
        int index = -1;
        Rectangle entityBound = entity.getComponent(BoundsComponent.class).bounds;
        BOUNDS_CENTER = boundary.getCenter(BOUNDS_CENTER);

        // Object can completely fit within the top quadrants
        boolean topQuadrant = entityBound.getY() > BOUNDS_CENTER.y;

        // Object can completely fit within the bottom quadrants
        boolean bottomQuadrant = entityBound.getY() + entityBound.getHeight() < BOUNDS_CENTER.y;

        // Object can completely fit within the left quadrants
        if(entityBound.getX() < BOUNDS_CENTER.x && entityBound.getX() + entityBound.getWidth() < BOUNDS_CENTER.x)
        {
            if(topQuadrant)
            {
                index = 0;
            }
            else if(bottomQuadrant)
            {
                index = 3;
            }
        }
        // Object can completely fit within the right quadrants
        else if(entityBound.getX() > BOUNDS_CENTER.x)
        {
            if(topQuadrant)
            {
                index = 1;
            }
            else if(bottomQuadrant)
            {
                index = 2;
            }
        }

        // If we get here, the object can not fit completely in a child node, and will be part of the parent node
        return index;
    }

    /**
     * Insert an entity into the appropriate node, subdividing if necessary.
     *
     * @param entity
     */

    public void insert(Entity entity)
    {
        // If we have any child nodes, see if the entity could be contained completely inside of one
        // of them
        if(childNodes.size > 0)
        {
            int index = getIndex(entity);

            // If full containment is possible, recursively insert in that node.
            if(index != -1)
            {
                childNodes.get(index).insert(entity);

                return;
            }
        }

        // Add the entity to the list of entities for the node we are in
        entities.add(entity);

        // If we've exceeded the max number of entities for this node (And have more that we could subdivide),
        // attempt to subdivide and insert further
        if(entities.size > MAX_ENTITIES && level < MAX_LEVELS)
        {
            // Only subdivide if we haven't
            if(childNodes.size == 0)
            {
                subdivide();
            }

            int i = 0;
            while(i < entities.size)
            {
                // Move and insert what we can into the child nodes. If it can't be fully contained in the
                // child nodes, leave it at this level.
                int index = getIndex(entities.get(i));
                if(index != -1)
                {
                    Entity poppedEntity = entities.removeIndex(i);
                    QuadTreeNode nodeToAddTo = childNodes.get(index);
                    nodeToAddTo.insert(poppedEntity);
                }
                else
                {
                    i++;
                }
            }
        }
    }

    /**
     * Return all entities that could collide with the given object
     */
    public Array<Entity> retrieve(Array<Entity> entitiesToReturn, Entity entity)
    {
        // If we have any child nodes, see if the entity could be contained completely inside of one
        // of them
        if(childNodes.size > 0)
        {
            int index = getIndex(entity);

            // If full containment is possible, recurse retrieval in that node.
            if(index != -1)
            {
                QuadTreeNode nodeToRetrieveFrom = childNodes.get(index);
                nodeToRetrieveFrom.retrieve(entitiesToReturn, entity);
            }
        }

        // Add all the entities of the node we are in.
        entitiesToReturn.addAll(entities);

        return entitiesToReturn;
    }

    /**
     * Renders the boundaries of all the quad tree nodes in postorder depth traversal fashion recursively
     *
     * @param shapeRenderer
     */
    public void render(ShapeRenderer shapeRenderer)
    {
        // Attempt to render each child node
        for(QuadTreeNode currNode : childNodes)
        {
            currNode.render(shapeRenderer);
        }

        // Set up the colors for the levels
        switch(level)
        {
            case 0:
            case 1:
                shapeRenderer.setColor(Color.ORANGE);
                break;

            case 2:
                shapeRenderer.setColor(Color.RED);
                break;

            case 3:
                shapeRenderer.setColor(Color.GREEN);
                break;

            case 4:
                shapeRenderer.setColor(Color.BLUE);
                break;

            case 5:
                shapeRenderer.setColor(Color.MAGENTA);
                break;
        }

        // Render the rect
        shapeRenderer.rect(boundary.x, boundary.y, boundary.width, boundary.height);
    }
}
