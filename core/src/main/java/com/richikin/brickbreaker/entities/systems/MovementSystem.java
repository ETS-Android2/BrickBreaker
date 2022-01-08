package com.richikin.brickbreaker.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.richikin.brickbreaker.entities.components.MovementComponent;
import com.richikin.brickbreaker.entities.components.StateComponent;
import com.richikin.brickbreaker.entities.components.TransformComponent;

public class MovementSystem extends IteratingSystem
{
    ComponentMapper<MovementComponent>  movementMapper;
    ComponentMapper<StateComponent>     stateMapper;
    ComponentMapper<TransformComponent> transformMapper;

    public MovementSystem()
    {
        super
            (
                Family.all
                    (
                        MovementComponent.class,
                        StateComponent.class,
                        TransformComponent.class
                    ).get());

        movementMapper  = ComponentMapper.getFor(MovementComponent.class);
        stateMapper     = ComponentMapper.getFor(StateComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
    }
}
