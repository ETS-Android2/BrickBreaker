package com.richikin.brickbreaker.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.GdxSprite;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.physics.CollisionObject;
import com.richikin.gdxutils.physics.aabb.AABBData;

public class CollisionUtils implements Disposable
{
    public CollisionUtils()
    {
    }

    public void initialise()
    {
    }

    public CollisionObject newObject()
    {
        return new CollisionObject();
    }

    public CollisionObject newObject(Rectangle rectangle)
    {
        return new CollisionObject(rectangle);
    }

    /**
     * Get a new object collision object.
     *
     * @param x         the x
     * @param y         the y
     * @param width     the width
     * @param height    the height
     * @param graphicID the graphic id
     *
     * @return the collision object
     */
    public CollisionObject newObject(int x, int y, int width, int height, GraphicID graphicID)
    {
        return new CollisionObject(x, y, width, height, graphicID);
    }

    /**
     * Tidy ALL currently active collision objects.
     */
    public void tidy()
    {
        if (AABBData.inst().bodies() != null)
        {
            for (int i = 0; i < AABBData.inst().bodies().size; i++)
            {
                if (AABBData.inst().bodies().get(i) != null)
                {
                    if (AABBData.inst().bodies().get(i).action == ActionStates._DEAD)
                    {
                        AABBData.inst().remove(i);
                    }
                }
            }
        }
    }

    /**
     * Tidy (Kill) ALL currently active collision objects.
     */
    public void tidyAll()
    {
        for (int i = 0; i < AABBData.inst().bodies().size; i++)
        {
            AABBData.inst().bodies().get(i).action = ActionStates._DEAD;
        }

        tidy();

        AABBData.inst().bodies().clear();
    }

    public boolean canCollide(GdxSprite entity, GdxSprite target)
    {
        return ((entity.collidesWith & target.bodyCategory) != 0)
         && ((target.collidesWith & entity.bodyCategory) != 0)
         && (entity.getIndex() != target.getIndex());
    }

    public boolean filter(short theEntityFlag, short theCollisionBoxFlag)
    {
        return ((theEntityFlag & theCollisionBoxFlag) != 0);
    }

    public int getXBelow(GdxSprite spriteObj)
    {
        return (int) ((spriteObj.getCollisionRectangle().getX() + (App.getMapData().tileWidth / 2)) / App.getMapData().tileWidth);
    }

    public int getYBelow(GdxSprite spriteObj)
    {
        int y = (int) ((spriteObj.sprite.getY() - (spriteObj.sprite.getY() % App.getMapData().tileHeight)) / App.getMapData().tileHeight);

        if ((spriteObj.sprite.getY() % App.getMapData().tileHeight) == 0)
        {
            y--;
        }

        return y;
    }

    @Override
    public void dispose()
    {
        tidyAll();
    }
}
