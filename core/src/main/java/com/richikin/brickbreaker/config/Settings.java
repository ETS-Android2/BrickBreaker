package com.richikin.brickbreaker.config;

import com.richikin.gdxutils.config.StandardSettings;
import com.richikin.gdxutils.logging.Trace;

public class Settings extends StandardSettings
{
    private static final String _PREFS_FILE_NAME = "com.richikin.brickbreaker.preferences";

    public Settings()
    {
        Trace.fileInfo();

        if (getPrefs() == null)
        {
            createPreferencesObject(_PREFS_FILE_NAME);
        }
    }
}
