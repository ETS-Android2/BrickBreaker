package com.richikin.brickbreaker.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.core.EndgameManager;
import com.richikin.brickbreaker.core.GameControlLoop;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.ui.GameCompletedPanel;
import com.richikin.gdxutils.core.ScreenID;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.graphics.camera.OrthoGameCamera;
import com.richikin.gdxutils.logging.StopWatch;
import com.richikin.gdxutils.logging.Trace;

public class MainScene extends AbstractBaseScene
{
    public GameCompletedPanel gameCompletedPanel;
    public EndgameManager     endgameManager;
    public GameControlLoop    gameControlLoop;
    public StopWatch          retryDelay;

    /*
     * boolean firstTime - TRUE if MainScene has
     * just been entered, i.e. a NEW Game.
     *
     * Setting this to true allows initialise() to
     * be called from show(), one time only. If false, then
     * initialise() will be bypassed but the rest of show()
     * will be processed.
     */
    public boolean firstTime;

    public MainScene()
    {
        super();

        firstTime = true;
    }

    @Override
    public void initialise()
    {
        if (firstTime)
        {
            Trace.divider('#');
            Trace.dbg("NEW GAME:");
            Trace.dbg("_DEVMODE: ", GdxApp.getAppConfig().isDevMode());
            Trace.dbg("_GODMODE: ", GdxApp.getAppConfig().isGodMode());
            Trace.divider('#');

            App.getLevelManager().prepareNewGame();

            gameCompletedPanel = new GameCompletedPanel();
            endgameManager     = new EndgameManager();
            gameControlLoop    = new GameControlLoop();

            App.getAppState().set(StateID._STATE_SETUP);
        }
    }

    @Override
    public void update()
    {
        switch (App.getAppState().peek())
        {
            // These are here in case there is a lag between appstate
            // being set to these values and control being passed
            // to a different scene.
            case _STATE_MAIN_MENU:
            case _STATE_CLOSING:
            {
            }
            break;

            // All relevant states which apply
            // to this scene.
            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_DEVELOPER_PANEL:
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            case _STATE_PREPARE_LEVEL_RETRY:
            case _STATE_LEVEL_RETRY:
            case _STATE_PREPARE_LEVEL_FINISHED:
            case _STATE_LEVEL_FINISHED:
            case _STATE_DEV_RESTART_LEVEL1:
            case _STATE_PREPARE_GAME_OVER_MESSAGE:
            case _STATE_GAME_OVER:
            case _STATE_GAME_FINISHED:
            case _STATE_END_GAME:
            {
                gameControlLoop.update();
            }
            break;

            default:
            {
                Trace.dbg("Unsupported game state: " + App.getAppState().peek());
            }
            break;
        }
    }

    @Override
    public void render(float delta)
    {
        super.update();

        App.getMapData().update();
        App.getGameProgress().update();

        if (App.getAppConfig().gameScreenActive())
        {
            update();

            super.render(delta);

            App.getWorldModel().worldStep();
        }
    }

    public void draw(final SpriteBatch spriteBatch, OrthoGameCamera camera)
    {
    }

    @Override
    public void show()
    {
        Trace.fileInfo();

        super.show();

        GdxSystem.inst().currentScreenID = ScreenID._GAME_SCREEN;
        App.getBaseRenderer().disableAllCameras();

        initialise();

        GdxSystem.inst().hideAndDisableBackButton();

        App.getAppState().set(StateID._STATE_SETUP);
    }

    @Override
    public void hide()
    {
        Trace.fileInfo();

        super.hide();
    }

    /**
     * Resets this scene for re-use.
     */
    public void reset()
    {
        Trace.fileInfo();

        firstTime = true;
    }

    @Override
    public void loadImages()
    {
        App.getBaseRenderer().backgroundImage = App.getAssets().loadSingleAsset(GameAssets._GAME_BACKGROUND_ASSET, Texture.class);
    }

    @Override
    public void dispose()
    {
        gameCompletedPanel = null;
        endgameManager     = null;
        gameControlLoop    = null;
        retryDelay         = null;

        super.dispose();
    }
}
