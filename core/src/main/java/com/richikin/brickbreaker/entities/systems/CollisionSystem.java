package com.richikin.brickbreaker.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.richikin.brickbreaker.entities.components.CollisionComponent;
import com.richikin.brickbreaker.entities.components.SpriteComponent;
import com.richikin.enumslib.GraphicID;

public class CollisionSystem extends IteratingSystem
{
    ComponentMapper<CollisionComponent> collisionMapper;

    public CollisionSystem()
    {
        super(Family.all(CollisionComponent.class).get());

        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        CollisionComponent cc = collisionMapper.get(entity);
        Entity collidedEntity = cc.collisionEntity;

        if (collidedEntity != null)
        {
            GraphicID type = collidedEntity.getComponent(SpriteComponent.class).type;

            switch(type)
            {
                case _ENEMY:
                case _OBSTACLE:
                {
                }
                break;

                default:
                    break;
            }
        }
    }
}
