package com.richikin.gdxutils.logging;

public enum SystemMeters
{
    //@formatter:off
    _ILLEGAL_GAME_MODE                      (0),
    _SOUND_LOAD_FAIL                        (1),
    _BAD_PLAYER_ACTION                      (2),
    _FONT_LOAD_FAILURE                      (3),
    _BORDERED_FONT_LOAD_FAILURE             (4),

    _IO_EXCEPTION                           (5),
    _INDEX_OUT_OF_BOUNDS_EXCEPTION          (6),
    _ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION    (7),
    _SAX_EXCEPTION                          (8),
    _INTERRUPTED_EXCEPTION                  (9),
    _NULL_POINTER_EXCEPTION                 (10),
    _ILLEGAL_STATE_EXCEPTION                (11),
    _GDX_RUNTIME_EXCEPTION                  (12),
    _ENTITY_DATA_EXCEPTION                  (13),

    _UNKNOWN_EXCEPTION                      (14),
    _DUMMY_METER                            (15),

    _MAX_SYSTEM_METERS(16);
    //@formatter:on

    private int meterNum;

    SystemMeters(int value)
    {
        setMeterNum(value);
    }

    public static SystemMeters fromValue(int value)
    {
        SystemMeters[] meters = values();

        for (SystemMeters meter : meters)
        {
            if (meter.get() == value)
            {
                return meter;
            }
        }

        return _DUMMY_METER;
    }

    public int get()
    {
        return meterNum;
    }

    private void setMeterNum(int meterNum)
    {
        this.meterNum = meterNum;
    }
}
