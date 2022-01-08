package com.richikin.gdxutils.physics.aabb;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.physics.CollisionObject;

public class AABBData implements Disposable
{
    private Array<CollisionObject> collisionBodies;

    // ------------------------------------------------------
    private static final AABBData instance = new AABBData();
    public static AABBData inst()
    {
        return instance;
    }
    // ------------------------------------------------------

    public AABBData()
    {
    }

    public Array<CollisionObject> bodies()
    {
        if (collisionBodies == null)
        {
            createData();
        }

        return collisionBodies;
    }

    public void add(CollisionObject object)
    {
        collisionBodies.add(object);
    }

    public void remove(int index)
    {
        collisionBodies.removeIndex(index);
        rescan();
    }

    public void rescan()
    {
        for (int i = 0; i < collisionBodies.size; i++)
        {
            collisionBodies.get(i).index = i;
        }
    }

    public void createData()
    {
        collisionBodies = new Array<>();
    }

    @Override
    public void dispose()
    {
        collisionBodies.clear();
        collisionBodies = null;
    }
}
