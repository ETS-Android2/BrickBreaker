package com.richikin.gdxutils.physics.aabb;

import com.richikin.gdxutils.physics.CollisionObject;

public interface ICollisionCallback
{
    void onPositiveCollision(CollisionObject cobjHitting);

    void onNegativeCollision();
}
