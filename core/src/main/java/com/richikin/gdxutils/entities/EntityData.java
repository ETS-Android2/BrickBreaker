package com.richikin.gdxutils.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.logging.Trace;

// TODO: 02/01/2022 - Class badly named.
public class EntityData implements Disposable
{
    private Array<IEntityComponent>        entityMap;
    private Array<IEntityManagerComponent> managerList;

    public EntityData()
    {
        entityMap   = new Array<>();
        managerList = new Array<>();
    }

    public void addEntity(IEntityComponent entity)
    {
        if (entity != null)
        {
            entityMap.add(entity);
        }
        else
        {
            Trace.dbg("***** Attempt to add NULL Object, EntityMap current size: " + entityMap.size);
        }
    }

    public IEntityComponent getEntity(int index)
    {
        return entityMap.get(index);
    }

    public void removeEntityAt(int index)
    {
        entityMap.removeIndex(index);
    }

    public int addManager(IEntityManagerComponent manager)
    {
        managerList.add(manager);

        return managerList.size - 1;
    }

    public IEntityManagerComponent getManager(int index)
    {
        return managerList.get(index);
    }

    public void removeManager(IEntityManagerComponent manager)
    {
        if (managerList.removeValue(manager, false))
        {
            Trace.err("FAILED to remove ", manager.getGID().name());
        }
    }

    public Array<IEntityComponent> getEntityMap()
    {
        return entityMap;
    }

    public Array<IEntityManagerComponent> getManagerList()
    {
        return managerList;
    }

    @Override
    public void dispose()
    {
        Trace.fileInfo();

        for (IEntityComponent component : entityMap)
        {
            component.dispose();
        }

        for (IEntityManagerComponent component : managerList)
        {
            component.dispose();
        }

        entityMap.clear();
        managerList.clear();

        entityMap = null;
        managerList = null;
    }
}
