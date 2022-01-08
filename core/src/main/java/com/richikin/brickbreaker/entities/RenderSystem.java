package com.richikin.brickbreaker.entities;

import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.Gfx;

public class RenderSystem
{
    public RenderSystem()
    {
    }

    /**
     * Draw all sprites to the scene.
     * Uses a Z-coord system: 0 at front, MAX at rear.
     * Any sprites with a Z value > MAX will not be drawn.
     */
    public void drawSprites()
    {
        if (App.getEntityManager().isEntityUpdateAllowed())
        {
            GdxSprite entity;

            for (int z = Gfx._MAXIMUM_Z_DEPTH - 1; z >= 0; z--)
            {
                for (int i = 0; i < App.getEntityData().getEntityMap().size; i++)
                {
                    entity = (GdxSprite) App.getEntityData().getEntity(i);

                    if ((entity != null) && (entity.zPosition == z))
                    {
                        entity.preDraw();

                        if (isInViewWindow(entity) && entity.isDrawable)
                        {
                            entity.draw(App.getSpriteBatch());
                        }
                    }
                }
            }
        }
    }

    // TODO: 21/10/2021
    public boolean isInViewWindow(GdxSprite entity)
    {
        return true;
    }
}
