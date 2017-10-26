package com.mygdx.game.utils.QuadTree;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.BoundsComponent;

public class Grid implements CollisionStructure {

    // The actual grid array
    private Array<Entity>[][] grid = null;
    private Array<Entity> arrayAux;

    // The rows and columns in the grid
    private int rows, cols;

    // The cell size
    private int cellSize;

    public Grid (int mapWidth, int mapHeight, int cellSize)
    {
        this.cellSize = cellSize;
        // Calculate rows and cols
        rows = (mapHeight + cellSize - 1) / cellSize;
        cols = (mapWidth + cellSize - 1) / cellSize;
        // Create the grid
        grid = new Array[cols][rows];
        initializeGrid();
        arrayAux = new Array<>();
    }

    private void initializeGrid() {
        for (int x=0; x<cols; x++)
        {
            for (int y=0; y<rows; y++)
            {
                grid[x][y] = new Array<>();
            }
        }
    }

    public void clear()
    {
        for (int x=0; x<cols; x++)
        {
            for (int y=0; y<rows; y++)
            {
                grid[x][y].clear();
            }
        }
    }

    @Override
    public void insert(Entity e)
    {
        Rectangle r = e.getComponent(BoundsComponent.class).bounds;

        int topLeftX = (int) Math.max(0, r.x / cellSize);
        int topLeftY = (int) Math.max(0, r.y / cellSize);
        int bottomRightX = (int) Math.min(cols-1, (r.x + r.width -1) / cellSize);
        int bottomRightY = (int) Math.min(rows-1, (r.y + r.height -1) / cellSize);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                grid[x][y].add(e);
            }
        }
    }

    public Array<Entity> retrieve(Array<Entity> retrieveList, Entity e)
    {
        retrieveList.clear();
        // Calculate the positions again
        Rectangle r = e.getComponent(BoundsComponent.class).bounds;

        int topLeftX = (int) Math.max(0, r.x / cellSize);
        int topLeftY = (int) Math.max(0, r.y / cellSize);
        int bottomRightX = (int) Math.min(cols-1, (r.x + r.width -1) / cellSize);
        int bottomRightY = (int) Math.min(rows-1, (r.y + r.height -1) / cellSize);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                Array<Entity> cell = grid[x][y];
                // Add every entity in the cell to the list
                for (int i=0; i<cell.size; i++)
                {
                    Entity rc = cell.get(i);
                    // Avoid duplicate entries

                    // No se si va false
                    if (!retrieveList.contains(rc, false))
                        retrieveList.add(rc);
                }
            }
        }
        return retrieveList;
    }

    public Entity getNearest(Entity e)
    {
        Rectangle entityReq = e.getComponent(BoundsComponent.class).bounds;
        // For comparisons
        Entity nearest = null;
        long distance = Long.MAX_VALUE;
        // Retrieve the entities
        Array<Entity> collidables = retrieve(arrayAux, e);
        // Iterate and find the nearest
        for (int i=0; i<collidables.size; i++)
        {
            Entity toCheck = collidables.get(i);

            Rectangle toCheckReq = toCheck.getComponent(BoundsComponent.class).bounds;

            // Check the distance
            long dist = (long) ((toCheckReq.x-entityReq.x)*(toCheckReq.x-entityReq.x) + (toCheckReq.y-entityReq.y)*(toCheckReq.y-entityReq.y));
            if (dist < distance)
            {
                nearest = toCheck;
                distance = dist;
            }
        }
        return nearest;
    }
}
