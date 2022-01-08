package com.richikin.gdxutils.graphics;

import com.badlogic.gdx.math.Vector2;
import com.richikin.gdxutils.maths.SimpleVec2F;

public class GfxData
{
    // ################################################################
    // region CONSTANTS
    //
    // Maximum Z-sorting depth for sprites
    public static final int _MAXIMUM_Z_DEPTH = 20;

    //
    // The desired Frame Rate
    public static final float _FPS     = 60f;
    public static final float _MIN_FPS = 30f;

    public static float _DEFAULT_ZOOM   = 1.0f;
    public static float _LERP_SPEED     = 0.075f;

    //
    // Values for Box2D.step()
    public static final float   _STEP_TIME           = (1.0f / 60f);
    public static final int     _VELOCITY_ITERATIONS = 8;
    public static final int     _POSITION_ITERATIONS = 3;
    public static final Vector2 _WORLD_GRAVITY       = new Vector2(0, -9.8f);
    public static final int     _FALL_GRAVITY        = 10;
    // endregion

    // #################################################################
    // Data to be initialised in setData().
    //
    public static int   _TERMINAL_VELOCITY;
    public static float _PIXELS_TO_METERS;

    public static float _HUD_SCENE_WIDTH;
    public static float _HUD_SCENE_HEIGHT;
    public static float _GAME_SCENE_WIDTH;
    public static float _GAME_SCENE_HEIGHT;

    // #################################################################
    // The following must be initialised in the local codebase.
    // Once that is done, setData() MUST be called.
    //
    // Pixels Per Meter in the Box2D World, usually the length of a single TiledMap tile.
    public static float _PPM;

    public static int _HUD_WIDTH;       // Width in pixels of the HUD
    public static int _HUD_HEIGHT;      // Height in pixels of the HUD
    public static int _DESKTOP_WIDTH;   // Width in pixels of the Desktop window
    public static int _DESKTOP_HEIGHT;  // Height in pixels of the Desktop window
    public static int _VIEW_WIDTH;      // Width in pixels of the game view
    public static int _VIEW_HEIGHT;     // Height in pixels of the game view

    public static SimpleVec2F pixelDimensions = new SimpleVec2F();
    public static SimpleVec2F meterDimensions = new SimpleVec2F();

    public static void setData()
    {
        _TERMINAL_VELOCITY = (int) (_PPM * _FALL_GRAVITY);
        _PIXELS_TO_METERS  = (1.0f / _PPM);

        _HUD_SCENE_WIDTH   = (_HUD_WIDTH / _PPM);
        _HUD_SCENE_HEIGHT  = (_HUD_HEIGHT / _PPM);
        _GAME_SCENE_WIDTH  = (_VIEW_WIDTH / _PPM);
        _GAME_SCENE_HEIGHT = (_VIEW_HEIGHT / _PPM);
    }

    public static void setPPM(final float newPPM)
    {
        if (newPPM != _PPM)
        {
            _PPM               = newPPM;
            _PIXELS_TO_METERS  = (1.0f / _PPM);
            _GAME_SCENE_WIDTH  = (_VIEW_WIDTH / _PPM);
            _GAME_SCENE_HEIGHT = (_VIEW_HEIGHT / _PPM);
        }
    }

    public static SimpleVec2F getScreenSizeInMeters()
    {
        meterDimensions.set(_VIEW_WIDTH * _PIXELS_TO_METERS, _VIEW_HEIGHT * _PIXELS_TO_METERS);

        return meterDimensions;
    }

    public static SimpleVec2F getScreenSizeInPixels()
    {
        pixelDimensions.set(_VIEW_WIDTH, _VIEW_HEIGHT);

        return pixelDimensions;
    }

    public static float PixelsToMeters(float pixels)
    {
        return pixels * _PIXELS_TO_METERS;
    }
}
