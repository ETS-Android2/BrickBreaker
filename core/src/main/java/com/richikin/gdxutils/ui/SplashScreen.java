package com.richikin.gdxutils.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.logging.StopWatch;
import com.richikin.gdxutils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class SplashScreen implements Disposable
{
    private static final SplashScreen _INSTANCE;

    // Instance initialiser block
    static
    {
        try
        {
            _INSTANCE = new SplashScreen();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean isAvailable;

    private SpriteBatch     batch;
    private Texture         background;
    private Texture         logo;
    private StopWatch       stopWatch;

    public static SplashScreen inst()
    {
        return _INSTANCE;
    }

    public void setup(final String imageName)
    {
        Trace.fileInfo();

        GfxData.setPPM(32.0f);

        batch      = new SpriteBatch();
        background = new Texture(imageName);
        logo       = new Texture("data/red7logo.png");
        stopWatch  = StopWatch.start();

        isAvailable = true;
    }

    public void update()
    {
        if (stopWatch.time(TimeUnit.MILLISECONDS) > 1500)
        {
            isAvailable = false;
        }
    }

    public void render()
    {
        if (isAvailable)
        {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            batch.draw
                (
                    background,
                    0,
                    0,
                    Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()
                );

            batch.draw
                (
                    logo,
                    (Gdx.graphics.getWidth() - logo.getWidth()) / 2f,
                    (Gdx.graphics.getHeight() - logo.getHeight()) / 2f
                );
            batch.end();
        }
    }

    @Override
    public void dispose()
    {
        Trace.fileInfo();

        batch.dispose();
        background.dispose();

        stopWatch    = null;
        batch        = null;
        background   = null;
    }
}
