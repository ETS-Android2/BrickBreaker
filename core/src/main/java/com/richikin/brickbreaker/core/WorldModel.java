package com.richikin.brickbreaker.core;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.physics.BodyBuilder;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.physics.box2d.Box2DContactListener;

public class WorldModel implements Disposable
{
    public World                box2DWorld;
    public Box2DDebugRenderer   b2dr;
    public Box2DContactListener box2DContactListener;
    public BodyBuilder bodyBuilder;

    public WorldModel()
    {
    }

    public void initialise()
    {
        Trace.fileInfo();

        if (App.getAppConfig().isUsingBox2DPhysics())
        {
            box2DWorld = new World
                (
                    new Vector2
                        (
                            (GfxData._WORLD_GRAVITY.x * GfxData._PPM),
                            (GfxData._WORLD_GRAVITY.y * GfxData._PPM)
                        ),
                    false
                );

            bodyBuilder          = new BodyBuilder();
            box2DContactListener = new Box2DContactListener();

            box2DWorld.setContactListener(box2DContactListener);
        }
    }

    public void createB2DRenderer()
    {
        if (App.getAppConfig().isDevMode() && App.getAppConfig().isUsingBox2DPhysics())
        {
            Trace.fileInfo();

            b2dr = new Box2DDebugRenderer
                (
                    true,
                    true,
                    true,
                    true,
                    false,
                    true
                );
        }
    }

    public void drawDebugMatrix()
    {
        if (App.getAppConfig().isUsingBox2DPhysics())
        {
            if (b2dr != null)
            {
                //
                // Care needed here if the viewport sizes for SpriteGameCamera
                // and TiledGameCamera are different.
                Matrix4 debugMatrix = App.getBaseRenderer().mainGameCamera.camera.combined.scale(GfxData._PPM, GfxData._PPM, 0);

                b2dr.render(box2DWorld, debugMatrix);
            }
        }
    }

    /**
     * Update this games Box2D physics.
     */
    public void worldStep()
    {
        if (box2DWorld != null)
        {
            box2DWorld.step(GfxData._STEP_TIME, GfxData._VELOCITY_ITERATIONS, GfxData._POSITION_ITERATIONS);
        }
    }

    @Override
    public void dispose()
    {
        Trace.fileInfo();

        b2dr.dispose();
        b2dr = null;

        box2DContactListener = null;
        bodyBuilder          = null;

        box2DWorld.dispose();
        box2DWorld = null;
    }
}

