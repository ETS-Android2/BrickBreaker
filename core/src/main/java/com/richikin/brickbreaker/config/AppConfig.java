package com.richikin.brickbreaker.config;

import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.core.LocalPlayServicesData;
import com.richikin.brickbreaker.scenes.MainScene;
import com.richikin.brickbreaker.scenes.TitleScene;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.config.IApplicationConfig;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.core.ScreenID;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.graphics.camera.Shake;
import com.richikin.gdxutils.input.controllers.ControllerType;
import com.richikin.gdxutils.logging.Stats;
import com.richikin.gdxutils.logging.Trace;

public class AppConfig implements IApplicationConfig
{
    private StateID developerPanelState = StateID._STATE_DISABLED;
    private boolean isAndroidOnDesktop  = false;
    private boolean isGodMode           = false;
    private boolean isDevMode           = false;

    public AppConfig()
    {
        Trace.fileInfo();
    }

    @Override
    public void setup()
    {
        Trace.fileInfo();

        App.createObjects();

        // ------------------------------------------------
        setDeveloperModeState();
        setTempDeveloperSettings();
        // ------------------------------------------------

        Gfx.initialise();
        GdxSystem.inst().setup();

        Stats.setup("com.richikin.brickbreaker.meters");

        //
        // These essential objects have now been created.
        // Setup/Initialise for any essential objects required
        // before TitleScene can be created is mostly
        // performed in startApp().
    }

    @Override
    public void startApp()
    {
        Trace.fileInfo();

        App.getWorldModel().initialise();
        App.getAssets().initialise();
        App.getSettings().freshInstallCheck();
        App.getSettings().debugReport();

        Gfx.setPPM(32.0f);

        if (GdxSystem.inst().isAndroidApp())
        {
            Trace.dbg("Initialising Google Play Services.");

            App.getPlayServices().setup();
            App.getPlayServices().createApiClient();

            LocalPlayServicesData lpsd = new LocalPlayServicesData();
            lpsd.setup();
        }

        GdxApp.getMapData().setup();

        App.getBaseRenderer().createCameras();
        App.createStage(App.getBaseRenderer().hudGameCamera.viewport);
        App.getAABBRenderer().setup(GameAssets._PRO_WINDOWS_FONT);
        App.getWorldModel().createB2DRenderer();
        App.getAudio().setup();
        App.getInputManager().setup();

        Shake.setAllowed(false);

        App.isStartupDone = true;

        Trace.divider();
    }

    /**
     * Ends the startup process by handing control
     * to the {@link TitleScene} or, if TitleScene
     * is disabled, control is passed to {@link MainScene}
     */
    @Override
    public void closeStartup()
    {
        configReport();

        App.setTitleScene(new TitleScene());

        //
        // Development option, to allow skipping of the main menu
        // and moving straight to the game scene.
        if (isDevMode() && App.getSettings().isDisabled(Settings._MENU_SCENE))
        {
            App.setMainScene(new MainScene());
            App.getMainScene().reset();
            App.getMainGame().setScreen(App.getMainScene());
        }
        else
        {
            App.getMainGame().setScreen(App.getTitleScene());
        }
    }

    private void setTempDeveloperSettings()
    {
        if (isDevMode())
        {
            Trace.divider();
            Trace.dbg("Temporary Development Settings");

            setAndroidOnDesktop(GdxSystem.inst().isDesktopApp());
            setGodMode(true);
//            setGodMode(false);

            final String[] disableList =
                {
                    Settings._BUTTON_BOXES,
                    Settings._B2D_RENDERER,
                    Settings._AUTOPLAY,
                    Settings._SHOW_DEBUG,
                    Settings._SHOW_FPS,
                    Settings._TILE_BOXES,
                    Settings._SPRITE_BOXES,
                    Settings._BOX2D_PHYSICS,
                    Settings._MENU_SCENE,
                };

            final String[] enableList =
                {
                    Settings._INSTALLED,
                };

            for (String str : disableList)
            {
                App.getSettings().disable(str);
            }

            for (String str : enableList)
            {
                App.getSettings().enable(str);
            }

            Trace.divider();
        }
    }

    /**
     * Setting _BOX2D_PHYSICS will override any
     * local AABB collision detection.
     */
    @Override
    public boolean isUsingBox2DPhysics()
    {
        return App.getSettings().isEnabled(Settings._BOX2D_PHYSICS);
    }

    /**
     *
     */
    @Override
    public boolean isUsingAshleyECS()
    {
        return App.getSettings().isEnabled(Settings._USING_ASHLEY_ECS);
    }

    /**
     * Reads the environment variable '_DEV_MODE', and
     * sets 'isDevMode' accordingly.
     * Note: Android builds default to _DEV_MODE = false.
     */
    @Override
    public void setDeveloperModeState()
    {
        if (GdxSystem.inst().isDesktopApp())
        {
            isDevMode = "TRUE".equalsIgnoreCase(System.getenv("_DEV_MODE"));
        }
        else
        {
            isDevMode = false;
        }

        Trace.dbg("Developer Mode is ", isDevMode ? "ENABLED." : "DISABLED.");
    }

    /**
     * Sets 'isDevMode' flag to the specified state.
     *
     * @param _state TRUE or FALSE.
     */
    @Override
    public void setDevMode(boolean _state)
    {
        isDevMode = _state;
    }

    /**
     * Sets 'isGodMode' flag to the specified state.
     *
     * @param _state TRUE or FALSE.
     */
    @Override
    public void setGodMode(boolean _state)
    {
        isGodMode = _state;
    }

    /**
     * If enabled, this flag allows testing of android related
     * tests on desktop builds.
     */
    @Override
    public void setAndroidOnDesktop(boolean _state)
    {
        isAndroidOnDesktop = _state;
    }

    /**
     * 'DEVeloper Mode' is only allowed on Desktop builds
     *
     * @return TRUE if enabled.
     */
    @Override
    public boolean isDevMode()
    {
        return GdxSystem.inst().isDesktopApp() && isDevMode;
    }

    /**
     * 'GOD Mode' is only allowed on Desktop builds
     *
     * @return TRUE if enabled.
     */
    @Override
    public boolean isGodMode()
    {
        return GdxSystem.inst().isDesktopApp() && isGodMode;
    }

    /**
     * Enables or disables the Developer Settings Panel.
     * The only valid states are:-
     * StateID._STATE_DISABLED
     * StateID._STATE_ENABLED
     * All other states will default to _STATE_DISABLED.
     *
     * @param state The panel State.
     */
    @Override
    public void setDeveloperPanelState(StateID state)
    {
        switch (state)
        {
            case _STATE_ENABLED:
            case _STATE_DISABLED:
            {
                developerPanelState = state;
            }
            break;

            default:
            {
                developerPanelState = StateID._STATE_DISABLED;
            }
            break;
        }
    }

    @Override
    public StateID getDeveloperPanelState()
    {
        return developerPanelState;
    }

    @Override
    public boolean isAndroidOnDesktop()
    {
        return isAndroidOnDesktop;
    }

    @Override
    public boolean gameScreenActive()
    {
        return GdxSystem.inst().currentScreenID == ScreenID._GAME_SCREEN;
    }

    @Override
    public void freshInstallCheck()
    {
        Trace.fileInfo();

        if (!App.getSettings().isEnabled(Settings._INSTALLED))
        {
            Trace.dbg("FRESH INSTALL.");

            Trace.dbg("Initialising all App settings to default values.");
            App.getSettings().resetToDefaults();

            Trace.dbg("Setting all Statistical logging meters to zero.");
            Stats.resetAllMeters();

            App.getSettings().enable(Settings._INSTALLED);
        }
    }

    /**
     * Pause the game
     */
    @Override
    public void pause()
    {
        App.getAppState().set(StateID._STATE_PAUSED);
        GdxSystem.inst().gamePaused = true;

        if (App.getHud().hudStateID != StateID._STATE_SETTINGS_PANEL)
        {
            App.getHud().hudStateID = StateID._STATE_PAUSED;
        }
    }

    /**
     * Un-pause the game
     */
    @Override
    public void unPause()
    {
        App.getAppState().set(StateID._STATE_GAME);
        GdxSystem.inst().gamePaused = false;
        App.getHud().hudStateID     = StateID._STATE_PANEL_UPDATE;
    }

    @Override
    public void configReport()
    {
        if (isDevMode())
        {
            Trace.divider();
            Trace.dbg("Android App         : " + GdxSystem.inst().isAndroidApp());
            Trace.dbg("Desktop App         : " + GdxSystem.inst().isDesktopApp());
            Trace.dbg("Android On Desktop  : " + isAndroidOnDesktop());
            Trace.divider();
            Trace.dbg("isDevMode()         : " + isDevMode());
            Trace.dbg("isGodMode()         : " + isGodMode());
            Trace.divider();
            Trace.dbg("_DESKTOP_WIDTH      : " + GfxData._DESKTOP_WIDTH);
            Trace.dbg("_DESKTOP_HEIGHT     : " + GfxData._DESKTOP_HEIGHT);
            Trace.dbg("_VIEW_WIDTH         : " + GfxData._VIEW_WIDTH);
            Trace.dbg("_VIEW_HEIGHT        : " + GfxData._VIEW_HEIGHT);
            Trace.dbg("_HUD_WIDTH          : " + GfxData._HUD_WIDTH);
            Trace.dbg("_HUD_HEIGHT         : " + GfxData._HUD_HEIGHT);
            Trace.dbg("_GAME_SCENE_WIDTH   : " + GfxData._GAME_SCENE_WIDTH);
            Trace.dbg("_GAME_SCENE_HEIGHT  : " + GfxData._GAME_SCENE_HEIGHT);
            Trace.dbg("_HUD_SCENE_WIDTH    : " + GfxData._HUD_SCENE_WIDTH);
            Trace.dbg("_HUD_SCENE_HEIGHT   : " + GfxData._HUD_SCENE_HEIGHT);
            Trace.divider();
            Trace.dbg("_PPM                : " + GfxData._PPM);
            Trace.divider();
            Trace.dbg("_VIRTUAL?           : " + GdxSystem.inst().availableInputs.contains(ControllerType._VIRTUAL, true));
            Trace.dbg("_EXTERNAL?          : " + GdxSystem.inst().availableInputs.contains(ControllerType._EXTERNAL, true));
            Trace.dbg("_KEYBOARD?          : " + GdxSystem.inst().availableInputs.contains(ControllerType._KEYBOARD, true));
            Trace.dbg("controllerPos       : " + GdxSystem.inst().virtualControllerPos);
            Trace.dbg("controllersFitted   : " + GdxSystem.inst().controllersFitted);
            Trace.dbg("usedController      : " + GdxSystem.inst().usedController);
            Trace.divider();
        }
    }
}
