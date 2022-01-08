package com.richikin.gdxutils.config;

import com.richikin.gdxutils.core.StateID;

public interface IApplicationConfig
{
    void setup();

    void startApp();

    void closeStartup();

    boolean isUsingBox2DPhysics();

    boolean isUsingAshleyECS();

    void setDeveloperModeState();

    void setDevMode(boolean _state);

    void setGodMode(boolean _state);

    void setAndroidOnDesktop(boolean _state);

    boolean isDevMode();

    boolean isGodMode();

    void setDeveloperPanelState(StateID state);

    StateID getDeveloperPanelState();

    boolean isAndroidOnDesktop();

    boolean gameScreenActive();

    void freshInstallCheck();

    void pause();

    void unPause();

    void configReport();
}
