package com.richikin.brickbreaker.core;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.brickbreaker.audio.AudioData;
import com.richikin.brickbreaker.config.Settings;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.brickbreaker.scenes.MainScene;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.input.controllers.ControllerType;
import com.richikin.gdxutils.logging.Trace;

// TODO: 30/12/2021 - Badly named, this is not a loop, but it IS the game control which is called IN a loop...
public class GameControlLoop
{
    private static final float _ZOOM_IN_START  = 10.0f;
    private static final float _ZOOM_OUT_START = 1.0f;
    private static final float _ZOOM_SPEED     = 0.25f;

    private float cameraZoom;
    private int badLuckMessage;

    public GameControlLoop()
    {
        Trace.fileInfo();
    }

    public void update()
    {
        switch (App.getAppState().peek())
        {
            //
            // Initialise the current level.
            // If the level is restarting, that will
            // also be handled here.
            case _STATE_SETUP:
            {
                stateSetup();
            }
            break;

            //
            // Display and update the 'Get Ready' message.
            case _STATE_GET_READY:
            {
                stateGetReady();
            }
            break;

            case _STATE_DEVELOPER_PANEL:
            case _STATE_SETTINGS_PANEL:
            case _STATE_PAUSED:
            case _STATE_GAME:
            {
                stateGame();
            }
            break;

            //
            // Player lost a life.
            // Trying again.
            case _STATE_PREPARE_LEVEL_RETRY:
            case _STATE_LEVEL_RETRY:
            {
                stateSetForRetry();
            }
            break;

            case _STATE_PREPARE_LEVEL_FINISHED:
            case _STATE_LEVEL_FINISHED:
            {
                stateSetForLevelFinished();
            }
            break;

            case _STATE_PREPARE_GAME_OVER_MESSAGE:
            {
                stateSetForGameOverMessage();
            }
            break;

            case _STATE_GAME_OVER:
            case _STATE_GAME_FINISHED:
            {
                stateWaitForGameOverMessage();
            }
            break;

            //
            // Back to MainMenuScreen
            case _STATE_END_GAME:
            {
                stateSetForEndGame();
            }
            break;

            case _STATE_DEV_RESTART_LEVEL1:
            {
                stateSetForRestartLevelOne();
            }
            break;

            default:
            {
                Trace.dbg("Unsupported gameState: " + App.getAppState().peek());
            }
            break;
        }
    }

    /**
     * Initialise the current level.
     * If the level is restarting, that will also be handled here.
     * _STATE_SETUP
     */
    private void stateSetup()
    {
        Trace.dbg("_STATE_SETUP: firstTime = ", scr().firstTime);

        App.getLevelManager().prepareCurrentLevel(scr().firstTime);

        if (scr().firstTime)
        {
            // All cameras ON
            App.getBaseRenderer().enableAllCameras();

            App.getAudio().playGameTune(true);

            setForZoomIn();
        }

        App.getHud().getPanelManager().addZoomPanel(GameAssets._GETREADY_MSG_ASSET, 1500);

        App.getAppState().set(StateID._STATE_GET_READY);
        App.getGameProgress().gameSetupDone = true;

        Trace.dbg("Setup done.");
    }

    /**
     * Display and update the 'Get Ready' message.
     * _STATE_GET_READY
     */
    private void stateGetReady()
    {
        App.getHud().update();

        //
        // If there is no 'Get Ready' message on screen then setup
        // flow control to play the game.
        if (!App.getHud().getPanelManager().panelExists(GameAssets._GETREADY_MSG_ASSET))
        {
            Trace.dbg("----- START GAME (GET READY) -----");

            App.getAppState().set(StateID._STATE_GAME);
            App.getHud().setStateID(StateID._STATE_PANEL_UPDATE);

            // If game has virtual/onscreen controls...
            if (GdxSystem.inst().availableInputs.contains(ControllerType._VIRTUAL, true))
            {
                App.getHud().showControls();
                App.getHud().showPauseButton(true);
            }

            scr().firstTime = false;
        }
        else
        {
            //
            // Zoom the game screen into view.
            if ((cameraZoom -= _ZOOM_SPEED) <= Gfx._DEFAULT_ZOOM)
            {
                cameraZoom = Gfx._DEFAULT_ZOOM;
            }

            App.getBaseRenderer().mainGameCamera.setCameraZoom(cameraZoom);
        }
    }

    /**
     * Update the game for states:-
     * _STATE_DEVELOPER_PANEL
     * _STATE_SETTINGS_PANEL
     * _STATE_PAUSED
     * _STATE_GAME
     */
    private void stateGame()
    {
        App.getHud().update();

        if (App.getAppState().peek() == StateID._STATE_DEVELOPER_PANEL)
        {
            if (App.getAppConfig().getDeveloperPanelState() == StateID._STATE_DISABLED)
            {
                App.getAppState().set(StateID._STATE_GAME);
                App.getHud().setStateID(StateID._STATE_PANEL_UPDATE);
            }
        }
        else
        {
            App.getEntityManager().updateSprites();
            App.getEntityManager().tidySprites();

            //
            // Check for game ending
            if (!scr().endgameManager.update())
            {
                //
                // Tasks to perform if the game has not ended
                if (App.getAppState().peek() == StateID._STATE_PAUSED)
                {
                    if (!GdxSystem.inst().gamePaused)
                    {
                        App.getAppState().set(StateID._STATE_GAME);
                    }
                }
            }
        }
    }

    /**
     * Handles the preparation for retrying the current
     * level, after Player loses a life.
     * _STATE_PREPARE_LEVEL_RETRY
     * _STATE_LEVEL_RETRY
     */
    private void stateSetForRetry()
    {
        App.getHud().update();

        if (App.getAppState().peek() == StateID._STATE_PREPARE_LEVEL_RETRY)
        {
            try
            {
                badLuckMessage = MathUtils.random(GameAssets.badLuckMessages.length - 1);
            }
            catch (ArrayIndexOutOfBoundsException boundsException)
            {
                badLuckMessage = 0;
            }

            App.getHud().getPanelManager().addZoomPanel(GameAssets.badLuckMessages[badLuckMessage], 2500);

            App.getAppState().set(StateID._STATE_LEVEL_RETRY);
        }
        else
        {
            if (!App.getHud().getPanelManager().panelExists(GameAssets.badLuckMessages[badLuckMessage]))
            {
                App.getAppState().set(StateID._STATE_SETUP);
            }
        }

        scr().retryDelay = null;
    }

    /**
     * Handles finishing the current level and
     * moving on to the next one.
     * _STATE_PREPARE_LEVEL_FINISHED:
     * _STATE_LEVEL_FINISHED:
     */
    private void stateSetForLevelFinished()
    {
        App.getHud().update();

        if (App.getAppState().peek() == StateID._STATE_PREPARE_LEVEL_FINISHED)
        {
            if (App.getGameProgress().stacksAreEmpty())
            {
                App.getHud().getPanelManager().addZoomPanel(GameAssets._AREA_CLEAR_MSG_ASSET, 2500);

                App.getAppState().set(StateID._STATE_LEVEL_FINISHED);
                App.getHud().setStateID(StateID._STATE_PANEL_UPDATE);
            }
            else
            {
                App.getGameProgress().update();
            }
        }
        else
        {
            if (!App.getHud().getPanelManager().panelExists(GameAssets._AREA_CLEAR_MSG_ASSET))
            {
                App.getLevelManager().closeCurrentLevel();
                App.getGameProgress().gameLevel++;

                if (++App.getMapCreator().mapLevel > App.getMapCreator().levelList.length)
                {
                    App.getMapCreator().mapLevel = 1;
                }

                App.getHud().update();

                App.getAppState().set(StateID._STATE_SETUP);
                App.getHud().setStateID(StateID._STATE_PANEL_START);
            }
        }
    }

    /**
     * Initialise the 'Game Over' message.
     * _STATE_PREPARE_GAME_OVER_MESSAGE
     */
    private void stateSetForGameOverMessage()
    {
        App.getHud().getPanelManager().addZoomPanel(GameAssets._GAMEOVER_MSG_ASSET, 3000);

        App.getAudio().startSound(AudioData.SFX_LOST);

        App.getAppState().set(StateID._STATE_GAME_OVER);
    }

    /**
     * Game Over, due to all levels being completed.
     * Game Over, due to losing all lives.
     * (Waits for the 'Game Over' message to disappear.)
     * _STATE_GAME_OVER
     * _STATE_GAME_FINISHED
     */
    private void stateWaitForGameOverMessage()
    {
        App.getHud().update();

        if (!App.getHud().getPanelManager().panelExists(GameAssets._GAMEOVER_MSG_ASSET))
        {
            App.getAppState().set(StateID._STATE_END_GAME);
        }
    }

    /**
     * Game Ended, hand control back to MainMenuScreen.
     * Control is also passed to here if forceQuitToMenu or quitToMainMenu are set.
     * _STATE_END_GAME
     */
    private void stateSetForEndGame()
    {
        Trace.divider();
        Trace.dbg("***** GAME OVER *****");
        Trace.divider();

        App.getAudio().playGameTune(false);

        if (App.getSettings().isDisabled(Settings._MENU_SCENE))
        {
            App.getAppState().set(StateID._STATE_DEV_RESTART_LEVEL1);
        }
        else
        {
            App.getMainGame().setScreen(App.getTitleScene());
        }
    }

    /**
     * Developer option.
     * Re-set the game to start at level 1.
     * _STATE_DEV_RESTART_LEVEL1
     */
    private void stateSetForRestartLevelOne()
    {
        if (App.getSettings().isDisabled(Settings._MENU_SCENE))
        {
            Trace.fileInfo();

            App.getGameProgress().resetProgress();
            App.getGameProgress().toDefaults();

            scr().firstTime = true;

            GdxSystem.inst().forceQuitToMenu = false;
            GdxSystem.inst().quitToMainMenu = false;

            App.getAppState().set(StateID._STATE_SETUP);
        }
    }

    private void setForZoomIn()
    {
        //
        // Start with the bricks zoomed out. They will zoom into view
        // while the 'get ready' message is displaying.
        cameraZoom = _ZOOM_IN_START;
        App.getBaseRenderer().mainGameCamera.setCameraZoom(cameraZoom);
    }

    private void setForZoomOut()
    {
        cameraZoom = _ZOOM_OUT_START;
        App.getBaseRenderer().mainGameCamera.setCameraZoom(cameraZoom);
    }

    private MainScene scr()
    {
        return App.getMainScene();
    }
}
