package com.richikin.brickbreaker.config;

import com.richikin.gdxutils.logging.SystemMeters;

public enum Meters
{
    //@formatter:off
    // -------------------------------------------
    _VALUE              (SystemMeters._MAX_SYSTEM_METERS.get()),

    // -------------------------------------------
    _SPEED_UP_BONUS     (_VALUE.meterNum),
    _SLOW_DOWN_BONUS    (_VALUE.meterNum + 1),
    _SHRINK_BONUS       (_VALUE.meterNum + 2),
    _EXPAND_BONUS       (_VALUE.meterNum + 3),
    _BALLS_X2_BONUS     (_VALUE.meterNum + 4),

    // -------------------------------------------
    _DUMMY_METER        (_VALUE.meterNum + 5);
    //@formatter:on

    private int meterNum;

    Meters(int value)
    {
        setMeterNum(value);
    }

    public static Meters fromValue(int value)
    {
        Meters[] meters = values();

        for (Meters meter : meters)
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
