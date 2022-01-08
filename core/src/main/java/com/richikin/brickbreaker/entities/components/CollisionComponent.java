package com.richikin.brickbreaker.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.physics.CollisionObject;
import com.richikin.gdxutils.physics.aabb.IAABBCollision;
import com.richikin.gdxutils.physics.aabb.ICollisionCallback;

public class CollisionComponent implements Component
{
    public short              bodyCategory      = Gfx.CAT_NOTHING;
    public short              collidesWith      = Gfx.CAT_NOTHING;
    public IAABBCollision     aabb              = null;
    public ICollisionCallback collisionCallback = null;
    public CollisionObject    collisionObject   = null;
    public Entity             collisionEntity   = null;
}
