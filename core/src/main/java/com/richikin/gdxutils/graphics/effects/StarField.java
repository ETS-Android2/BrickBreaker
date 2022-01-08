package com.richikin.gdxutils.graphics.effects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.logging.Trace;

public class StarField implements Disposable
{
    public final float speed    = 40f;
    public final int   numStars = 5000;

    private Array<StarObject> stars;

    public StarField()
    {
        this.stars = new Array<>();

        for (int i = 0; i < numStars; i++)
        {
            stars.add(new StarObject());
        }
    }

    public void render()
    {
        for (StarObject star : stars)
        {
            star.render(speed);
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        Trace.fileInfo();

        for (StarObject starObject : stars)
        {
            starObject.dispose();
        }

        stars.clear();
        stars = null;
    }
}
