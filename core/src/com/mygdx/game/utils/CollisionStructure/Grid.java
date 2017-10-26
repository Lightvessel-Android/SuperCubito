package com.mygdx.game.utils.CollisionStructure;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.BoundsComponent;
import com.mygdx.game.components.TransformComponent;

import kotlin.Pair;

public class Grid implements CollisionStructure {

    // The actual grid array
    private Array<Entity>[][] grid = null;
    private Array<Entity> arrayAux;
    private Rectangle rAux;

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
        rAux = new Rectangle(0,0,0,0);
    }

    @Override
    public void insert(Entity e)
    {
        Rectangle r = e.getComponent(BoundsComponent.class).bounds;

        Pair<Integer, Integer> pos = calculatePosition(r);

        grid[pos.getFirst()][pos.getSecond()].add(e);
    }

    @Override
    public void update(Entity entity) {
        removeOld(entity);
        insert(entity);
    }

    private void removeOld(Entity entity) {

        Rectangle req = entity.getComponent(BoundsComponent.class).bounds;
        Vector2 lastPos = entity.getComponent(TransformComponent.class).lastPosition;

        rAux.set(lastPos.x, lastPos.y, req.width, req.height);

        removeEntityWithReq(rAux, entity);
    }

    public void remove(Entity entity) {
        Rectangle req = entity.getComponent(BoundsComponent.class).bounds;

        removeEntityWithReq(req, entity);
    }

    private void removeEntityWithReq(Rectangle req, Entity entity) {
        Pair<Integer, Integer> pos = calculatePosition(req);

        grid[pos.getFirst()][pos.getSecond()].removeValue(entity, false);
    }


    public Array<Entity> retrieve(Array<Entity> retrieveList, Entity e)
    {
        retrieveList.clear();
        Rectangle r = e.getComponent(BoundsComponent.class).bounds;

        Pair<Integer, Integer> pos = calculatePosition(r);

        Array<Entity> cell = grid[pos.getFirst()][pos.getSecond()];

        // Add every entity in the cell to the list
        for (int i=0; i<cell.size; i++)
        {
            Entity rc = cell.get(i);
            // Avoid duplicate entries
            // No se si va false
            if (!retrieveList.contains(rc, false))
                retrieveList.add(rc);
        }
        return retrieveList;
    }

    private Pair<Integer, Integer> calculatePosition(Rectangle rec){
        int topLeftX = (int) Math.max(0, rec.x / cellSize);
        int topLeftY = (int) Math.max(0, rec.y / cellSize);
        int bottomRightX = (int) Math.min(cols-1, (rec.x + rec.width -1) / cellSize);
        int bottomRightY = (int) Math.min(rows-1, (rec.y + rec.height -1) / cellSize);

        for (int x = topLeftX; x <= bottomRightX; x++)
        {
            for (int y = topLeftY; y <= bottomRightY; y++)
            {
                return new Pair<>(x,y);
            }
        }
        return new Pair<>(0,0);
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
}
