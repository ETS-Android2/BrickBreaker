package com.richikin.brickbreaker.core;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.Item;
import com.richikin.gdxutils.maths.NumberUtils;

public class GameProgress implements Disposable
{
    public boolean isRestarting;            // TRUE If the game is restarting, i.e from losing a life
    public boolean levelCompleted;          // ...
    public boolean gameCompleted;           // ...
    public boolean gameOver;                // ...
    public boolean gameSetupDone;           // ...
    public int     gameLevel;               // ...
    public float   gameDifficulty;          // Value used to modify game difficulty(enemy speeds, fire rates etc).
    public int     bricksDestroyed;         // ...

    public Item score;                      // ...
    public Item lives;                      // ...

    //
    // Stacks are used to allow counting up/down
    // (visually) of scores etc.
    public enum Stack
    {
        _SCORE,
        _LIVES
    }

    private int scoreStack;
    private int livesStack;

    public GameProgress()
    {
        score = new Item(0, GameConstants._MAX_SCORE, 0);
        lives = new Item(0, GameConstants._MAX_LIVES, GameConstants._MAX_LIVES);

        scoreStack = 0;
        livesStack = 0;

        resetProgress();
        toDefaults();
    }

    public void update()
    {
        switch (App.getAppState().peek())
        {
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_PREPARE_LEVEL_FINISHED:
            case _STATE_MESSAGE_PANEL:
            {
                if (isRestarting)
                {
                    App.getPlayer().setActionState(ActionStates._RESETTING);
                }

                updateStacks();
                updateDifficulty();
            }
            break;

            default:
                break;
        }
    }

    public void resetProgress()
    {
        isRestarting    = false;
        levelCompleted  = false;
        gameCompleted   = false;
        gameOver        = false;
        gameSetupDone   = false;
        bricksDestroyed = 0;
    }

    public void toDefaults()
    {
        gameLevel      = 1;
        gameDifficulty = 1.0f;

        scoreStack = 0;
        livesStack = 0;

        score.setToMinimum();
        lives.setToMaximum();
    }

    public void closeLastGame()
    {
        Trace.fileInfo();

        App.getPlayServices().submitScore(score.getTotal(), gameLevel);

        resetProgress();
        toDefaults();
    }

    public void stackPush(Stack stack, int amount)
    {
        if (stack == Stack._SCORE)
        {
            scoreStack += amount;
        }
        else
        {
            if (stack == Stack._LIVES)
            {
                livesStack += amount;
            }
        }
    }

    private void updateStacks()
    {
        int amount;

        if (scoreStack > 0)
        {
            amount = NumberUtils.getCount(scoreStack);

            score.add(amount);
            scoreStack -= amount;
        }

        if (livesStack > 0)
        {
            lives.add(1);
            livesStack--;
        }
    }

    public boolean stacksAreEmpty()
    {
        return ((scoreStack == 0) && (livesStack == 0));
    }

    private void updateDifficulty()
    {
        // TODO: 06/12/2021
        gameDifficulty += 0.001f;
    }

    @Override
    public void dispose()
    {
        score = null;
        lives = null;
    }
}
