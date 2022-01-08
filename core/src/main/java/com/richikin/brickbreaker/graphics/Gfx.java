package com.richikin.brickbreaker.graphics;

import com.richikin.gdxutils.graphics.GfxData;

public class Gfx extends GfxData
{
    //
    // Entity collision types
    public static final short CAT_NOTHING       = 0x0000;   // - 00 (0     )
    public static final short CAT_PLAYER        = 0x0001;   // - 01 (1     )
    public static final short CAT_PLAYER_WEAPON = 0x0002;   // - 02 (2     )
    public static final short CAT_FIXED_ENEMY   = 0x0004;   // - 03 (4     )
    public static final short CAT_WALL          = 0x0008;   // - 04 (8     )
    public static final short CAT_PICKUP        = 0x0010;   // - 05 (16    )
    public static final short _UNDEFINED_6      = 0x0020;   // - 06 (32    )
    public static final short _UNDEFINED_7      = 0x0040;   // - 07 (64    )
    public static final short _UNDEFINED_8      = 0x0080;   // - 08 (128   )
    public static final short _UNDEFINED_9      = 0x0100;   // - 09 (256   )
    public static final short _UNDEFINED_10     = 0x0200;   // - 10 (512   )
    public static final short _UNDEFINED_11     = 0x0400;   // - 11 (1024  )
    public static final short _UNDEFINED_12     = 0x0800;   // - 12 (2048  )
    public static final short _UNDEFINED_13     = 0x1000;   // - 13 (4096  )
    public static final short _UNDEFINED_14     = 0x2000;   // - 14 (8192  )
    public static final short _UNDEFINED_15     = 0x4000;   // - 15 (16384 )

    public static int _LEFT_BOUNDARY;
    public static int _RIGHT_BOUNDARY;

    public static void initialise()
    {
        setPPM(32.0f);

        GfxData.setData();
    }

    public static void setAndroidDimensions()
    {
        _VIEW_WIDTH     = 1616;
        _VIEW_HEIGHT    = 2480;
        _HUD_WIDTH      = 640;
        _HUD_HEIGHT     = 992;
        _DESKTOP_WIDTH  = 480;
        _DESKTOP_HEIGHT = 640;

        _LEFT_BOUNDARY  = 64;
        _RIGHT_BOUNDARY = (_VIEW_WIDTH - 64);
    }

    public static void setDesktopDimensions()
    {
        _VIEW_WIDTH     = 202 * 8;
        _VIEW_HEIGHT    = 64 * 40;
        _HUD_WIDTH      = 640;
        _HUD_HEIGHT     = 992;
        _DESKTOP_WIDTH  = 480;
        _DESKTOP_HEIGHT = 640;

        _LEFT_BOUNDARY  = 64;
        _RIGHT_BOUNDARY = (_VIEW_WIDTH - 64);
    }
}
