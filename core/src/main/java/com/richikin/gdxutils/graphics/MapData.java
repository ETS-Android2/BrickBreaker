package com.richikin.gdxutils.graphics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec2;

public class MapData implements Disposable
{
    // ---------------------------------------------------------------------
    public int mapWidth;
    public int mapHeight;
    public int tileWidth;
    public int tileHeight;

    public Rectangle  viewportBox;
    public Rectangle  entityWindow;
    public SimpleVec2 mapPosition;
    // ---------------------------------------------------------------------

    public MapData()
    {
        Trace.fileInfo();
    }

    public void setup()
    {
        viewportBox     = new Rectangle();
        entityWindow    = new Rectangle();
        mapPosition     = new SimpleVec2();
    }

    /**
     * Update the screen virtual window. This box is used for checking
     * that entities are visible on screen.
     * An extended virtual window is also updated, which is larger than
     * the visible screen, and can be used for tracking entities that
     * are nearby.
     * These windows will NOT be updated if the MainPlayer has
     * not been initialised, as they use its map position.
     */
    public void update()
    {
        if (App.getPlayer() != null)
        {
            viewportBox.set
                (
                    (App.getPlayer().getPosition().x - (Gfx._VIEW_WIDTH / 2f)),
                    (App.getPlayer().getPosition().y - (Gfx._VIEW_HEIGHT / 2f)),
                    Gfx._VIEW_WIDTH,
                    Gfx._VIEW_HEIGHT
                );

            entityWindow.set
                (
                    (App.getPlayer().getPosition().x - (Gfx._VIEW_WIDTH + (Gfx._VIEW_WIDTH / 2f))),
                    (App.getPlayer().getPosition().y - (Gfx._VIEW_HEIGHT + (Gfx._VIEW_HEIGHT / 2f))),
                    (Gfx._VIEW_WIDTH * 3),
                    (Gfx._VIEW_HEIGHT * 3)
                );
        }
    }

    @Override
    public void dispose()
    {
        viewportBox  = null;
        entityWindow = null;
        mapPosition  = null;
    }
}
