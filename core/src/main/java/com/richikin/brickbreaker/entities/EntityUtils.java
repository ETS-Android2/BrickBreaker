package com.richikin.brickbreaker.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.entities.IEntityComponent;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec3;
import com.richikin.gdxutils.physics.Movement;
import com.richikin.gdxutils.physics.box2d.B2DConstants;

public class EntityUtils
{
    public EntityUtils()
    {
    }

    public TextureRegion getKeyFrame(final Animation<? extends TextureRegion> animation, final float elapsedTime, final boolean looping)
    {
        return animation.getKeyFrame(elapsedTime, looping);
    }

    public void resetAllPositions()
    {
        if (App.getEntityData().getEntityMap() != null)
        {
            GdxSprite entity;

            for (int i = 0; i < App.getEntityData().getEntityMap().size; i++)
            {
                //
                // This should only reset Paddle and Ball sprites
                if (App.getEntityData().getEntity(i).getType() == GraphicID._MAIN)
                {
                    entity = (GdxSprite) App.getEntityData().getEntity(i);

                    entity.sprite.setPosition(entity.initXYZ.getX(), entity.initXYZ.getY());
                }
            }
        }
    }

    /**
     * Fetch an initial Z position for the specified ID.
     *
     * @param graphicID The GraphicID.
     * @return Z position range is between 0 and Gfx._MAXIMUM_Z_DEPTH.
     */
    public int getInitialZPosition(GraphicID graphicID)
    {
        // TODO: 07/12/2020

        return 0;
    }

    public boolean isOnScreen(GdxSprite spriteObject)
    {
        return App.getMapData().viewportBox.overlaps(spriteObject.sprite.getBoundingRectangle());
    }

    public void tidy()
    {
        for (int i = 0; i < App.getEntityData().getEntityMap().size; i++)
        {
            if (App.getEntityData().getEntity(i).getActionState() == ActionStates._DEAD)
            {
                App.getEntityData().removeEntityAt(i);
            }
        }
    }

    public Animation<TextureRegion> createAnimation(String filename, TextureRegion[] destinationFrames, int frameCount, Animation.PlayMode playmode)
    {
        Animation<TextureRegion> animation;

        try
        {
            TextureRegion textureRegion = App.getAssets().getAnimationRegion(filename);

            TextureRegion[] tmpFrames = textureRegion.split
                (
                    (textureRegion.getRegionWidth() / frameCount),
                    textureRegion.getRegionHeight()
                )[0];

            System.arraycopy(tmpFrames, 0, destinationFrames, 0, frameCount);

            animation = new Animation<>(0.75f / 6f, tmpFrames);
            animation.setPlayMode(playmode);
        }
        catch (NullPointerException npe)
        {
            Trace.dbg("Creating animation from " + filename + " failed!");

            animation = null;
        }

        return animation;
    }

    /**
     * Gets a random sprite from the entity map, making
     * sure to not return the specified sprite.
     */
    public GdxSprite getRandomSprite(GdxSprite oneToAvoid)
    {
        GdxSprite randomSprite;

        do
        {
            randomSprite = (GdxSprite) App.getEntityData().getEntity(MathUtils.random(App.getEntityData().getEntityMap().size - 1));
        }
        while ((randomSprite.gid == oneToAvoid.gid)
            || (randomSprite.sprite == null)
            || (randomSprite.getIndex() == oneToAvoid.getIndex()));

        return randomSprite;
    }

    /**
     * Finds the nearest sprite of type gid to the player.
     */
    public GdxSprite findNearest(GraphicID gid)
    {
        GdxSprite distantSprite = findFirstOf(gid);

        if (distantSprite != null)
        {
            float distance = App.getPlayer().getPosition().dst(distantSprite.getPosition());

            for (IEntityComponent entity : App.getEntityData().getEntityMap())
            {
                if (entity.getGID() == gid)
                {
                    GdxSprite gdxSprite = (GdxSprite) entity;

                    float tempDistance = App.getPlayer().getPosition().dst(gdxSprite.getPosition());

                    if (Math.abs(tempDistance) < Math.abs(distance))
                    {
                        distance      = tempDistance;
                        distantSprite = gdxSprite;
                    }
                }
            }
        }

        return distantSprite;
    }

    /**
     * Finds the furthest sprite of type gid to the player.
     */
    public GdxSprite getDistantSprite(GdxSprite checkSprite)
    {
        GdxSprite distantSprite = App.getPlayer();

        float distance = checkSprite.getPosition().dst(distantSprite.getPosition());

        for (IEntityComponent entity : App.getEntityData().getEntityMap())
        {
            GdxSprite gdxSprite    = (GdxSprite) entity;
            float     tempDistance = checkSprite.getPosition().dst(gdxSprite.getPosition());

            if (Math.abs(tempDistance) > Math.abs(distance))
            {
                distance      = tempDistance;
                distantSprite = gdxSprite;
            }
        }

        return distantSprite;
    }

    public GdxSprite findFirstOf(final GraphicID gid)
    {
        GdxSprite gdxSprite = null;

        for (IEntityComponent entity : App.getEntityData().getEntityMap())
        {
            if (entity.getGID() == gid)
            {
                gdxSprite = (GdxSprite) entity;
                break;
            }
        }

        return gdxSprite;
    }

    public GdxSprite findLastOf(final GraphicID gid)
    {
        GdxSprite gdxSprite = null;

        for (IEntityComponent entity : App.getEntityData().getEntityMap())
        {
            if (entity.getGID() == gid)
            {
                gdxSprite = (GdxSprite) entity;
            }
        }

        return gdxSprite;
    }

    public int findNumberOfGid(final GraphicID gid)
    {
        int count = 0;

        for (IEntityComponent entity : App.getEntityData().getEntityMap())
        {
            if (entity.getGID() == gid)
            {
                count++;
            }
        }

        return count;
    }

    public int findNumberOfType(final GraphicID type)
    {
        int count = 0;

        for (IEntityComponent entity : App.getEntityData().getEntityMap())
        {
            if (entity.getType() == type)
            {
                count++;
            }
        }

        return count;
    }

    public void addDynamicPhysicsBody(GdxSprite sprite)
    {
        if (App.getAppConfig().isUsingBox2DPhysics())
        {
            sprite.b2dBody = App.getWorldModel().bodyBuilder.createDynamicBox
                (
                    sprite,
                    1.0f,
                    B2DConstants.DEFAULT_FRICTION,
                    B2DConstants.DEFAULT_RESTITUTION
                );
        }
    }

    public void addKinematicPhysicsBody(GdxSprite sprite)
    {
        if (App.getAppConfig().isUsingBox2DPhysics())
        {
            sprite.b2dBody = App.getWorldModel().bodyBuilder.createKinematicBody
                (
                    sprite,
                    1.0f,
                    B2DConstants.DEFAULT_RESTITUTION
                );
        }
    }

    public void addStaticPhysicsBody(GdxSprite sprite)
    {
        if (App.getAppConfig().isUsingBox2DPhysics())
        {
            sprite.b2dBody = App.getWorldModel().bodyBuilder.createStaticBody(sprite);
        }
    }

    /**
     * Wrap an entities position in the map if it
     * has gone beyond either if the maps borders.
     */
    public void wrap(GdxSprite sprite)
    {
        if ((sprite.direction.getX() == Movement._DIRECTION_LEFT) && ((sprite.sprite.getX() + sprite.frameWidth) < 0))
        {
            sprite.sprite.translateX(App.getMapData().mapWidth * Movement._DIRECTION_RIGHT);
        }
        else
        {
            if ((sprite.direction.getX() == Movement._DIRECTION_RIGHT) && (sprite.sprite.getX() > App.getMapData().mapWidth))
            {
                sprite.sprite.translateX(App.getMapData().mapHeight * Movement._DIRECTION_LEFT);
            }
        }
    }

    public boolean canUpdate(GraphicID graphicID)
    {
        // TODO: 07/12/2020

        return true;
    }

    private boolean isEntityEnabled(GraphicID graphicID)
    {
        // TODO: 07/12/2020

        return true;
    }

    public SimpleVec3 translateMapPosToEntityWindow(GdxSprite sprite)
    {
        // TODO: 07/12/2020

        return new SimpleVec3();
    }
}
