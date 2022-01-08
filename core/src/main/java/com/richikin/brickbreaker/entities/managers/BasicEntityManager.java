package com.richikin.brickbreaker.entities.managers;

import com.richikin.gdxutils.entities.IEntityManagerComponent;
import com.richikin.enumslib.GraphicID;

public class BasicEntityManager implements IEntityManagerComponent
{
    protected GraphicID managerID;
    protected GraphicID graphicID;
    protected boolean   canPlace;
    protected int       activeCount;
    protected int       maxCount;

    public BasicEntityManager()
    {
        this.graphicID = GraphicID.G_NO_ID;
        this.managerID = GraphicID.G_NO_ID;
    }

    public BasicEntityManager(final GraphicID _graphicID)
    {
        this.graphicID = _graphicID;
        this.managerID = _graphicID;
    }

    @Override
    public void init()
    {
        activeCount = 0;
        maxCount = 1;
        canPlace = false;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
    }

    @Override
    public void free()
    {
        activeCount = Math.max(0, activeCount - 1);
    }

    @Override
    public void free(GraphicID gid)
    {
    }

    @Override
    public void reset()
    {
        activeCount = 0;
        maxCount = 1;
    }

    @Override
    public int getActiveCount()
    {
        return activeCount;
    }

    @Override
    public void setActiveCount(int numActive)
    {
        activeCount = numActive;
    }

    @Override
    public void addMaxCount(int add)
    {
        maxCount += add;
    }

    @Override
    public void setMaxCount(int max)
    {
        maxCount = max;
    }

    @Override
    public GraphicID getGID()
    {
        return managerID;
    }

    @Override
    public boolean isPlaceable()
    {
        return canPlace;
    }

    @Override
    public void setPlaceable(boolean placeable)
    {
        canPlace = placeable;
    }

    @Override
    public void dispose()
    {
        managerID = null;
        graphicID = null;
    }
}
