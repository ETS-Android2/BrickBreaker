package com.richikin.brickbreaker.core;

import com.richikin.gdxutils.core.ActionStates;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.logging.StopWatch;
import com.richikin.gdxutils.logging.Trace;

public class EndgameManager
{
    public EndgameManager()
    {
    }

    public boolean update()
    {
        boolean returnFlag = false;

        if (App.getBricksManager().getActiveCount() == 0)
        {
            App.getGameProgress().levelCompleted = true;
        }

        //
        // Player is dead, no lives left
        if (((App.getPlayer() != null) && (App.getPlayer().getActionState() == ActionStates._DEAD))
            || GdxSystem.inst().forceQuitToMenu)
        {
            Trace.divider();
            Trace.dbg("PLAYER IS DEAD, NO LIVES LEFT");
            Trace.divider();

            App.getAppState().set(StateID._STATE_PREPARE_GAME_OVER_MESSAGE);

            GdxSystem.inst().quitToMainMenu = true;

            returnFlag = true;
        }
        else
        {
            //
            // Waheyy!! All levels completed!
            if (App.getGameProgress().gameCompleted)
            {
                Trace.divider();
                Trace.dbg("GAME COMPLETED");
                Trace.divider();

//                App.mainScene.completedPanel = new GameCompletedPanel();
//                App.mainScene.completedPanel.setup();

                App.getHud().setStateID(StateID._STATE_GAME_FINISHED);
                App.getAppState().set(StateID._STATE_GAME_FINISHED);

                returnFlag = true;
            }
            else if (App.getGameProgress().levelCompleted)
            {
                Trace.divider();
                Trace.dbg("LEVEL COMPLETED");
                Trace.divider();

                App.getHud().setStateID(StateID._STATE_PANEL_UPDATE);
                App.getAppState().set(StateID._STATE_PREPARE_LEVEL_FINISHED);

                returnFlag = true;
            }
            //
            // Restarting due to life lost and
            // player is resetting...
            else if (App.getGameProgress().isRestarting)
            {
                assert App.getPlayer() != null;

                if (App.getPlayer().getActionState() == ActionStates._RESETTING)
                {
                    Trace.divider();
                    Trace.dbg("LIFE LOST - TRY AGAIN");
                    Trace.divider();

                    App.getMainScene().retryDelay = StopWatch.start();
                    App.getAppState().set(StateID._STATE_PREPARE_LEVEL_RETRY);
                }

                returnFlag = true;
            }
        }

        return returnFlag;
    }
}
