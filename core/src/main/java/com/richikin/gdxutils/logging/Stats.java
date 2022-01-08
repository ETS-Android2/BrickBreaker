package com.richikin.gdxutils.logging;

import com.badlogic.gdx.Gdx;

public class Stats
{
    private static com.badlogic.gdx.Preferences prefs;

    public static void setup(String prefsFileName)
    {
        Trace.fileInfo();

        prefs = Gdx.app.getPreferences(prefsFileName);
    }

    public static void setMeter(int meter, int amount)
    {
        if (prefs != null)
        {
            prefs.putInteger(SystemMeters.fromValue(meter).name(), amount);
            prefs.flush();
        }
    }

    public static void addToMeter(int meter, int amount)
    {
        if (prefs != null)
        {
            prefs.putInteger
                (
                    SystemMeters.fromValue(meter).name(),
                    (prefs.getInteger(SystemMeters.fromValue(meter).name(), 0) + amount)
                );

            prefs.flush();
        }
    }

    public static void decMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger
                (
                    SystemMeters.fromValue(meter).name(),
                    (prefs.getInteger(SystemMeters.fromValue(meter).name(), 0) - 1)
                );

            prefs.flush();
        }
    }

    public static void incMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger
                (
                    SystemMeters.fromValue(meter).name(),
                    (prefs.getInteger(SystemMeters.fromValue(meter).name(), 0) + 1)
                );

            prefs.flush();
        }
    }

    public static void clearMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger(SystemMeters.fromValue(meter).name(), 0);
            prefs.flush();
        }
    }

    public static int getMeter(int meter)
    {
        return (prefs == null) ? 0 : prefs.getInteger(SystemMeters.fromValue(meter).name(), 0);
    }

    public static void resetAllMeters()
    {
        if (prefs != null)
        {
            for (int i = 0; i < SystemMeters._MAX_SYSTEM_METERS.get(); i++)
            {
                prefs.putInteger(SystemMeters.fromValue(i).name(), 0);
            }

            prefs.flush();
        }
    }
}
