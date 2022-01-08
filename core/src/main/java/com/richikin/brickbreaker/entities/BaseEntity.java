package com.richikin.brickbreaker.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.entities.IEntityComponent;
import com.richikin.gdxutils.maths.SimpleVec3;
import com.richikin.gdxutils.physics.CollisionObject;
import com.richikin.gdxutils.physics.aabb.IAABBCollision;
import com.richikin.gdxutils.physics.aabb.ICollisionCallback;

public class BaseEntity implements IEntityComponent
{
    // -----------------------------------------------
    // properties etc
    //
    public GraphicID    gid;                        // Entity ID
    public GraphicID    type;                       // Entity Type - _ENTITY, _PICKUP, _OBSTACLE, etc.
    public ActionStates entityAction;               // Current action/state
    public int          spriteNumber;               // Position in the EntityMap array
    public int          frameWidth;                 // Width in pixels, or width of frame for animations
    public int          frameHeight;                // Width in pixels, or width of frame for animations
    public SimpleVec3   position;                   // Map position
    public int          link;
    public boolean      isLinked;

    // -----------------------------------------------
    // Collision Related
    //
    public Body               b2dBody;              // Box2D Physics body
    public CollisionObject    collisionObject;      // ...
    public ICollisionCallback collisionCallback;
    public IAABBCollision     aabb;
    public short              bodyCategory;         // Bit-mask entity collision type (See Gfx()).
    public short              collidesWith;         // Bit-mask of entity types that can be collided with

    // -----------------------------------------------
    // CODE
    //

    public BaseEntity()
    {
        this(GraphicID.G_NO_ID);
    }

    public BaseEntity(GraphicID gid)
    {
        this.gid = gid;
    }

    @Override
    public void setActionState(ActionStates action)
    {
        this.entityAction = action;
    }

    @Override
    public ActionStates getActionState()
    {
        return this.entityAction;
    }

    @Override
    public CollisionObject getCollisionObject()
    {
        return collisionObject;
    }

    @Override
    public void setCollisionObject(float xPos, float yPos)
    {
        setCollisionObject((int) xPos, (int) yPos);
    }

    @Override
    public void setCollisionObject(int xPos, int yPos)
    {
        collisionObject = App.getCollisionUtils().newObject
            (
                new Rectangle(xPos, yPos, frameWidth, frameHeight)
            );

        collisionObject.gid          = this.gid;
        collisionObject.type         = GraphicID._ENTITY;
        collisionObject.isObstacle   = false;
        collisionObject.parentEntity = this;
    }

    @Override
    public Rectangle getCollisionRectangle()
    {
        return collisionObject.rectangle;
    }

    @Override
    public void tidy(int index)
    {
    }

    @Override
    public float getTopEdge()
    {
        return position.y + frameHeight;
    }

    @Override
    public float getRightEdge()
    {
        return position.x + frameWidth;
    }

    @Override
    public short getBodyCategory()
    {
        return bodyCategory;
    }

    @Override
    public short getCollidesWith()
    {
        return collidesWith;
    }

    @Override
    public void setIndex(int index)
    {
        spriteNumber = index;
    }

    @Override
    public int getIndex()
    {
        return spriteNumber;
    }

    @Override
    public GraphicID getGID()
    {
        return gid;
    }

    @Override
    public GraphicID getType()
    {
        return type;
    }

    @Override
    public void dispose()
    {
    }
}
