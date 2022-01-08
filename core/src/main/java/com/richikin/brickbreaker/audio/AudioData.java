package com.richikin.brickbreaker.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioData
{
    /**
     * Standard Audio properties.
     */
    public static final int _SILENT               = 0;
    public static final int _MIN_VOLUME           = 0;
    public static final int _MAX_VOLUME           = 10;
    public static final int _VOLUME_INCREMENT     = 1;
    public static final int _VOLUME_MULTIPLIER    = 10;
    public static final int _DEFAULT_MUSIC_VOLUME = 4;
    public static final int _DEFAULT_FX_VOLUME    = 6;

    public static final int SFX_HIT       = 0;
    public static final int SFX_LOST      = 1;
    public static final int SFX_PICKUP    = 2;
    public static final int SFX_EXTRALIFE = 3;
    public static final int MAX_SOUND     = 4;

    public static final int MUS_TITLE   = 0;
    public static final int MUS_HISCORE = -1;
    public static final int MUS_GAME    = -1;
    public static final int MAX_TUNES   = 1;

    public static Sound[] sounds;
    public static Music[] music;

    // ----------------------------------------------

    public static void tidy()
    {
        sounds = null;
        music = null;
    }
}
