package com.richikin.brickbreaker.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.richikin.brickbreaker.config.AppConfig;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.gdxutils.google.DummyAdsController;
import com.richikin.gdxutils.google.IPlayServices;
import com.richikin.gdxutils.google.PlayServicesData;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.ui.SplashScreen;

public class MainGame extends com.badlogic.gdx.Game
{
    private SplashScreen splashScreen;

    public MainGame(IPlayServices _services)
    {
        if (_services != null)
        {
            App.setPlayServices(_services);
            App.setAdsController(new DummyAdsController());
            App.setPlayServicesData(new PlayServicesData());
        }
    }

    @Override
    public void create()
    {
        //
        // Initialise DEBUG classes
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Trace.enableWriteToFile(true);
        Trace.openDebugFile("documents/log.txt", true);
        Trace.fileInfo();

        App.setMainGame(this);

        //
        // Show the Red7Projects Logo screen
        // while objects are created etc.
        splashScreen = new SplashScreen();
        splashScreen.setup(GameAssets._SPLASH_SCREEN_ASSET);

        //
        // Initialise all essential objects required before
        // the main screen is initialised.
        App.setAppConfig(new AppConfig());
        App.getAppConfig().setup();
    }

    @Override
    public void render()
    {
        if (splashScreen.isAvailable)
        {
            if (!App.isStartupDone)
            {
                App.getAppConfig().startApp();
            }

            splashScreen.update();
            splashScreen.render();

            if (!splashScreen.isAvailable)
            {
                App.getAppConfig().closeStartup();
                splashScreen.dispose();
            }
        }
        else
        {
            super.render();
        }
    }

    @Override
    public void setScreen(Screen screen)
    {
        Trace.fileInfo();

        super.setScreen(screen);
    }
}
