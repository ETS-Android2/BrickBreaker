package com.richikin.brickbreaker.entities.components;

import com.badlogic.ashley.core.Component;
import com.richikin.gdxutils.maths.SimpleVec2F;
import com.richikin.gdxutils.physics.Direction;

public class MovementComponent implements Component
{
    public Direction   direction;
    public Direction   lookingAt;
    public SimpleVec2F distance;
    public SimpleVec2F speed;
}
