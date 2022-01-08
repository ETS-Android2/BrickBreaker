package com.richikin.gdxutils.physics.aabb;

import com.richikin.gdxutils.physics.CollisionObject;

public interface IAABBCollision
{
    boolean checkAABBBoxes(CollisionObject boxA);

    void dispose();
}
