package com.richikin.gdxutils.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.richikin.gdxutils.core.ScreenID;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.input.GameButtonRegion;
import com.richikin.gdxutils.input.Switch;
import com.richikin.gdxutils.input.controllers.ControllerPos;
import com.richikin.gdxutils.input.controllers.ControllerType;
import com.richikin.gdxutils.ui.Scene2DUtils;

public class GdxSystem
{
    public boolean               quitToMainMenu;             // Game over, back to menu screen
    public boolean               forceQuitToMenu;            // Quit to main menu, forced via pause mode for example.
    public boolean               gamePaused;                 // TRUE / FALSE Game Paused flag
    public boolean               camerasReady;               // TRUE when all cameras have been created.
    public boolean               shutDownActive;             // TRUE if game is currently processing EXIT request.
    public boolean               entitiesExist;              // Set true when all entities have been created
    public boolean               controllersFitted;          // TRUE if external controllers are fitted/connected.
    public boolean               gameButtonsReady;           // TRUE When all game buttons have been defined
    public ScreenID              currentScreenID;            // ID of the currently active screeen
    public String                usedController;             // The name of the controller being used
    public ControllerPos         virtualControllerPos;       // Virtual (on-screen) joystick position (LEFT or RIGHT)
    public Array<ControllerType> availableInputs;            // ...
    public GameButtonRegion      fullScreenButton;           // ...
    public Switch                systemBackButton;           // ...
    public ImageButton           backButton;                 // ...

    // -------------------------------------------------------
    private static final GdxSystem instance = new GdxSystem();

    public static GdxSystem inst()
    {
        return instance;
    }
    // -------------------------------------------------------

    public void setup()
    {
        quitToMainMenu    = false;
        forceQuitToMenu   = false;
        gamePaused        = false;
        camerasReady      = false;
        shutDownActive    = false;
        entitiesExist     = false;
        controllersFitted = false;
        gameButtonsReady  = false;
        usedController    = "None";
        availableInputs   = new Array<>();

        if (isDesktopApp() || GdxApp.getAppConfig().isAndroidOnDesktop())
        {
            Gdx.graphics.setWindowedMode(GfxData._DESKTOP_WIDTH, GfxData._DESKTOP_HEIGHT);
        }

        if (isAndroidApp())
        {
            availableInputs.add(ControllerType._VIRTUAL);
            virtualControllerPos = ControllerPos._LEFT;
        }
        else
        {
            availableInputs.add(ControllerType._EXTERNAL);
            availableInputs.add(ControllerType._KEYBOARD);
            virtualControllerPos = ControllerPos._HIDDEN;
        }

        fullScreenButton = new GameButtonRegion(0, 0, GfxData._HUD_WIDTH, GfxData._HUD_HEIGHT);
        systemBackButton = new Switch();
    }

    public void exit()
    {
        availableInputs.clear();
        availableInputs = null;

        usedController       = null;
        virtualControllerPos = null;
        fullScreenButton     = null;
        systemBackButton     = null;

        Gdx.app.exit();
    }

    /**
     * @return TRUE if the app is running on Desktop
     */
    public boolean isDesktopApp()
    {
        return (Gdx.app.getType() == Application.ApplicationType.Desktop);
    }

    /**
     * @return TRUE if the app is running on Android
     */
    public boolean isAndroidApp()
    {
        return (Gdx.app.getType() == Application.ApplicationType.Android);
    }

    /**
     * @return TRUE If an external controller is fitted
     */
    public boolean isControllerFitted()
    {
        return controllersFitted;
    }

    public void addBackButton(String _default, String _pressed)
    {
        Scene2DUtils scene2DUtils = new Scene2DUtils();

        backButton = scene2DUtils.addButton(_default, _pressed, 0, 0);
    }

    public void showAndEnableBackButton()
    {
        if (backButton != null)
        {
            backButton.setVisible(true);
            backButton.setDisabled(false);
        }
    }

    public void hideAndDisableBackButton()
    {
        if (backButton != null)
        {
            backButton.setVisible(false);
            backButton.setDisabled(true);
        }
    }
}
