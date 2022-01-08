package com.richikin.gdxutils.core;

import com.badlogic.gdx.utils.Disposable;

public interface ISettings extends Disposable
{
    void createPreferencesObject(String prefsName);

    boolean isEnabled(final String preference);

    boolean isDisabled(final String preference);

    void enable(final String preference);

    void disable(final String preference);

    void resetToDefaults();

    void freshInstallCheck();

    com.badlogic.gdx.Preferences getPrefs();

    void debugReport();
}
