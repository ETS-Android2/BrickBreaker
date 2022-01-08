package com.richikin.brickbreaker.core;

import com.richikin.brickbreaker.entities.characters.Ball;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.physics.aabb.AABBData;

public class LevelManager
{
    private boolean isFirstTime;

    public LevelManager()
    {
        isFirstTime = true;
    }

    /**
     * Prepare the current level by setting up maps, entities
     * and any relevant flags/variables.
     */
    public void prepareCurrentLevel(boolean firstTime)
    {
        Trace.fileInfo();
        Trace.info("firstTime: ", firstTime);
        Trace.info("isRestarting: ", App.getGameProgress().isRestarting);
        Trace.info("levelCompleted: ", App.getGameProgress().levelCompleted);

        if (App.getGameProgress().isRestarting)
        {
            restartCurrentLevel();
        }
        else if (firstTime || App.getGameProgress().levelCompleted)
        {
            setupForNewLevel();
        }

        GdxSystem.inst().gamePaused      = false;
        GdxSystem.inst().quitToMainMenu  = false;
        GdxSystem.inst().forceQuitToMenu = false;

        App.getGameProgress().isRestarting    = false;
        App.getGameProgress().levelCompleted  = false;
        App.getGameProgress().gameOver        = false;

        if (firstTime)
        {
            // Probably not needed for this game, but this is where
            // HUD items such as health bars etc. can be reset.
            App.getHud().refillItems();
            App.getHud().update();
        }
    }

    /**
     * Sets up the map and entities for a new level.
     */
    public void setupForNewLevel()
    {
        Trace.fileInfo();

        App.getCollisionUtils().initialise();
        App.getMapCreator().initialiseLevelMap();        // Load tiled map and create renderer
        App.getMapCreator().createPositioningData();     // Process the tiled map data
        App.getEntityManager().initialiseForLevel();
    }

    /**
     * Reset all entity positions, and re-init
     * the main player, ready to replay the current
     * level.
     */
    public void restartCurrentLevel()
    {
        Trace.fileInfo();

        App.getEntityUtils().resetAllPositions();

        App.getPlayer().setup(false);

        for (Ball ball : App.getEntities().balls)
        {
            ball.setup(true);
        }
    }

    /**
     * Actions to perform when a level has been completed.
     * Remove all entities/pickups/etc from the level, but
     * make sure that the main player is untouched.
     */
    public void closeCurrentLevel()
    {
        Trace.fileInfo();

        App.getEntityData().getEntityMap().setSize(1);
        AABBData.inst().bodies().setSize(1);

        App.getEntities().balls.clear();

        App.getMapCreator().placementTiles.clear();
        App.getMapCreator().currentMap.dispose();

        Trace.dbg("EntityMap Size: ", App.getEntityData().getEntityMap().size);
    }

    /**
     * Set up everything necessary for a new game,
     * called in MainScene#initialise.
     */
    public void prepareNewGame()
    {
        if (isFirstTime)
        {
            Trace.fileInfo();

            //
            // Make sure all progress counters are initialised.
            App.getGameProgress().resetProgress();

            //
            // Initialise the room that the game will start in.
            App.getRoomManager().initialise();

            App.getBaseRenderer().disableAllCameras();
            App.getBaseRenderer().hudGameCamera.isInUse = true;
            App.getBaseRenderer().isDrawingStage        = false;

            App.getEntities().setup();
            App.getEntityManager().initialise();
            App.getMapData().update();

            // Score, Lives display etc.
            App.getHud().createHud();
        }

        isFirstTime = false;
    }
}
