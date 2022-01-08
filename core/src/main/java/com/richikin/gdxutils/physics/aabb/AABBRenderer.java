package com.richikin.gdxutils.physics.aabb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.config.StandardSettings;
import com.richikin.gdxutils.graphics.text.FontUtils;
import com.richikin.gdxutils.logging.SystemMeters;
import com.richikin.gdxutils.logging.Stats;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.physics.CollisionObject;

public class AABBRenderer implements Disposable
{
    private TextureRegion debugTextureRegion;
    private BitmapFont    font;

    public AABBRenderer()
    {
    }

    public void setup(String debugFont)
    {
        debugTextureRegion = new TextureRegion();

        FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont(debugFont, 15, Color.WHITE);
    }

    public void drawBoxes()
    {
        if (GdxApp.getSettings().isEnabled(StandardSettings._TILE_BOXES))
        {
            drawTileLayerBoxes();
        }

        if (GdxApp.getSettings().isEnabled(StandardSettings._SPRITE_BOXES))
        {
            drawSpriteCollisionBoxes();
        }
    }

    private void drawTileLayerBoxes()
    {
        Rectangle debugRectangle = new Rectangle();

        for (int i = 0; i < GdxApp.getEntityData().getEntityMap().size; i++)
        {
            CollisionObject collisionObject = GdxApp.getEntityData().getEntity(i).getCollisionObject();

            if ((collisionObject.gid == GraphicID._GROUND)
                || (collisionObject.gid == GraphicID._WALL))
            {
                debugRectangle.set(collisionObject.rectangle);

                if (collisionObject.action == ActionStates._HITTABLE)
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getRedObjectName());
                }
                else if (collisionObject.action == ActionStates._HITTING)
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getBlueObjectName());
                }
                else
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getWhiteObjectName());
                }

                drawRect
                    (
                        debugTextureRegion,
                        (int) debugRectangle.x,
                        (int) debugRectangle.y,
                        (int) debugRectangle.width,
                        (int) debugRectangle.height,
                        4
                    );
            }
        }
    }

    private void drawSpriteCollisionBoxes()
    {
        CollisionRect debugRectangle = new CollisionRect(GraphicID.G_NO_ID);

        for (int i = 0; i < GdxApp.getEntityData().getEntityMap().size; i++)
        {
            CollisionObject collisionObject = GdxApp.getEntityData().getEntity(i).getCollisionObject();

            if (collisionObject.type == GraphicID._ENTITY)
            {
                debugRectangle.set(collisionObject.rectangle);

                if (collisionObject.rectangle.colour == Color.BLUE)
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getBlueObjectName());
                }
                else if ((collisionObject.rectangle.colour == Color.RED) || (collisionObject.action == ActionStates._HITTING))
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getRedObjectName());
                }
                else if (collisionObject.rectangle.colour == Color.YELLOW)
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getYellowObjectName());
                }
                else if (collisionObject.rectangle.colour == Color.GREEN)
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getGreenObjectName());
                }
                else
                {
                    debugTextureRegion = GdxApp.getAssets().getObjectRegion(GdxApp.getAssets().getWhiteObjectName());
                }

                drawRect
                    (
                        debugTextureRegion,
                        (int) debugRectangle.x,
                        (int) debugRectangle.y,
                        (int) debugRectangle.width,
                        (int) debugRectangle.height,
                        4
                    );
            }
        }
    }

    public void drawRect(int x, int y, int width, int height, int thickness)
    {
        debugTextureRegion = GdxApp.getAssets().getObjectRegion("solid_red32x32");

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    public void drawRect(int x, int y, int width, int height, int thickness, Color color)
    {
        String asset;

        if (color == Color.BLUE)
        {
            asset = GdxApp.getAssets().getBlueObjectName();
        }
        else if (color == Color.RED)
        {
            asset = GdxApp.getAssets().getRedObjectName();
        }
        else if (color == Color.YELLOW)
        {
            asset = GdxApp.getAssets().getYellowObjectName();
        }
        else if (color == Color.GREEN)
        {
            asset = GdxApp.getAssets().getGreenObjectName();
        }
        else
        {
            asset = GdxApp.getAssets().getWhiteObjectName();
        }

        debugTextureRegion = GdxApp.getAssets().getObjectRegion(asset);

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    private void drawRect(TextureRegion textureRegion, int x, int y, int width, int height, int thickness)
    {
        try
        {
            GdxApp.getSpriteBatch().draw(textureRegion, x, y, width, thickness);
            GdxApp.getSpriteBatch().draw(textureRegion, x, y, thickness, height);
            GdxApp.getSpriteBatch().draw(textureRegion, x, (y + height) - thickness, width, thickness);
            GdxApp.getSpriteBatch().draw(textureRegion, (x + width) - thickness, y, thickness, height);
        }
        catch (NullPointerException exception)
        {
            Trace.dbg("NullPointerException !");
            Trace.dbg("textureRegion: " + textureRegion);
            Trace.dbg("x: " + x);
            Trace.dbg("y: " + y);
            Trace.dbg("width: " + width);
            Trace.dbg("height: " + height);
            Trace.dbg("thickness: " + thickness);
            Trace.dbg("From: " + new Exception().getStackTrace()[1].getClassName());

            Stats.incMeter(SystemMeters._NULL_POINTER_EXCEPTION.get());
        }
    }

    @Override
    public void dispose()
    {
        Trace.fileInfo();

        debugTextureRegion.getTexture().dispose();
        debugTextureRegion = null;

        font.dispose();
        font = null;
    }
}
