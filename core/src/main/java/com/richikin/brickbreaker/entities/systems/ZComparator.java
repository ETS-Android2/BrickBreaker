package com.richikin.brickbreaker.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.richikin.brickbreaker.entities.components.TransformComponent;

import java.util.Comparator;

public class ZComparator implements Comparator<Entity>
{
    private final ComponentMapper<TransformComponent> mapper;

    public ZComparator()
    {
        mapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB)
    {
        float az  = mapper.get(entityA).position.z;
        float bz  = mapper.get(entityB).position.z;

        int result = 0;

        if (az > bz)
        {
            result = 1;
        }
        else if (az < bz)
        {
            result = -1;
        }

        return result;
    }
}
