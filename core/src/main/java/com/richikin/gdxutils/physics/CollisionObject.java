package com.richikin.gdxutils.physics;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.entities.IEntityComponent;
import com.richikin.gdxutils.logging.StopWatch;
import com.richikin.gdxutils.physics.aabb.AABBData;
import com.richikin.gdxutils.physics.aabb.CollisionRect;

import java.util.concurrent.TimeUnit;

public class CollisionObject
{
    public static final short _TOP    = 0x01;
    public static final short _BOTTOM = 0x02;
    public static final short _LEFT   = 0x04;
    public static final short _RIGHT  = 0x08;

    /*
     * Collision box status.
     *
     * _COLLIDABLE  -   Collidable but NOT in collision.
     * _INACTIVE    -   Permanently Invisible to any collidable objects.
     * _COLLIDING   -   In Collision.
     * _DEAD        -   To be removed from the list.
     */
    public ActionStates     action;
    public GraphicID        gid;                // ID of THIS object
    public GraphicID        type;               // _OBSTACLE or _ENTITY
    public short            contactMask;        //
    public CollisionRect    rectangle;          // The actual collision rectangle
    public IEntityComponent parentEntity;       // The GdxSprite this collision object belongs to, if applicable.
    public IEntityComponent contactEntity;      // ID of contact object
    public int              index;              // This objects position in the collision object arraylist

    public GraphicID idTop;                     // ID of object hitting the top of this object
    public GraphicID idBottom;                  // ID of object hitting the bottom of this object
    public GraphicID idLeft;                    // ID of object hitting the left of this object
    public GraphicID idRight;                   // ID of object hitting the right of this object

    public int boxHittingTop;
    public int boxHittingBottom;
    public int boxHittingLeft;
    public int boxHittingRight;

    public boolean isHittingPlayer;
    public boolean isObstacle;
    public boolean isContactObstacle;

    public  boolean   isInvisibilityAllowed;
    public  StopWatch invisibilityTimer;
    private int       invisibilityDelay;    // How long this collision object is ignored for

    public CollisionObject()
    {
        this.rectangle = new CollisionRect(GraphicID.G_NO_ID);

        create();
    }

    public CollisionObject(Rectangle rectangle)
    {
        this.rectangle = new CollisionRect(rectangle, GraphicID.G_NO_ID);

        create();
    }

    public CollisionObject(int x, int y, int width, int height, GraphicID type)
    {
        rectangle = new CollisionRect(new Rectangle(x, y, width, height), type);

        create();
    }

    public void kill()
    {
        action = ActionStates._DEAD;
    }

    public void clearCollision()
    {
        if (action != ActionStates._DEAD)
        {
            action          = ActionStates._HITTABLE;
            isHittingPlayer = false;

            contactEntity = null;
            contactMask   = 0;

            boxHittingTop    = 0;
            boxHittingBottom = 0;
            boxHittingLeft   = 0;
            boxHittingRight  = 0;

            idTop    = GraphicID.G_NO_ID;
            idBottom = GraphicID.G_NO_ID;
            idLeft   = GraphicID.G_NO_ID;
            idRight  = GraphicID.G_NO_ID;
        }
    }

    public void setInvisibility(int timeInMilliseconds)
    {
        action            = ActionStates._INVISIBLE;
        invisibilityDelay = timeInMilliseconds;
        invisibilityTimer.reset();
    }

    public void checkInvisibility()
    {
        if ((action == ActionStates._HITTING) && isInvisibilityAllowed)
        {
            if (invisibilityTimer.time(TimeUnit.MILLISECONDS) >= invisibilityDelay)
            {
                action = ActionStates._HITTABLE;
            }
        }
    }

    private void create()
    {
        clearCollision();

        index                 = AABBData.inst().bodies().size;
        isObstacle            = true;
        isContactObstacle     = false;
        gid                   = GraphicID.G_NO_ID;
        invisibilityTimer     = StopWatch.start();
        isInvisibilityAllowed = true;
    }

    public boolean hasContactUp()
    {
        return (contactMask & _TOP) > 0;
    }

    public boolean hasContactDown()
    {
        return (contactMask & _BOTTOM) > 0;
    }

    public boolean hasContactLeft()
    {
        return (contactMask & _LEFT) > 0;
    }

    public boolean hasContactRight()
    {
        return (contactMask & _RIGHT) > 0;
    }

    public IEntityComponent getParent()
    {
        return parentEntity;
    }

    public IEntityComponent getContact()
    {
        return contactEntity;
    }
}
