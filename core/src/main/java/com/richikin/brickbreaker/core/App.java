package com.richikin.brickbreaker.core;

import com.richikin.brickbreaker.audio.AudioData;
import com.richikin.brickbreaker.audio.GameAudio;
import com.richikin.brickbreaker.config.Settings;
import com.richikin.brickbreaker.entities.Entities;
import com.richikin.brickbreaker.entities.EntityManager;
import com.richikin.brickbreaker.entities.EntityUtils;
import com.richikin.brickbreaker.entities.characters.Ball;
import com.richikin.brickbreaker.entities.characters.Paddle;
import com.richikin.brickbreaker.entities.managers.BallManager;
import com.richikin.brickbreaker.entities.managers.BricksManager;
import com.richikin.brickbreaker.entities.managers.PickupManager;
import com.richikin.brickbreaker.graphics.BaseRenderer;
import com.richikin.brickbreaker.graphics.LocalAssetsData;
import com.richikin.brickbreaker.input.InputManager;
import com.richikin.brickbreaker.maps.MapCreator;
import com.richikin.brickbreaker.maps.RoomManager;
import com.richikin.brickbreaker.maps.TiledUtils;
import com.richikin.brickbreaker.physics.CollisionUtils;
import com.richikin.brickbreaker.scenes.MainScene;
import com.richikin.brickbreaker.scenes.TitleScene;
import com.richikin.brickbreaker.ui.HeadsUpDisplay;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.logging.Trace;

public class App extends GdxApp
{
    // -------------------------------------------------
    // Objects created on power-up (Essential Objects)
    // Including (from GdxApp):-
    //      - MainGame
    //          - GoogleServices (for android builds)
    //          - DUmmyAdsController (for android builds)
    //          - PlayServicesData (for android builds)
    //      - AppConfig (creates the following:-)
    //          - StateManager
    //          - Settings
    //          - SpriteBatch
    //          - Assets/AssetManager
    //          - Audio
    //          - Stage (requires BaseRenderer setting up first)
    //          - EntityData
    //          - HighScoreutils
    private static BaseRenderer baseRenderer;
    private static MainGame     mainGame;
    private static WorldModel   worldModel;
    private static TitleScene   titleScene;
    private static InputManager inputManager;

    // -------------------------------------------------
    // Objects created in TitleScene
    private static MainScene mainScene;

    // -------------------------------------------------
    // Objects created in MainScene.
    // These will be destroyed in TitleScene if a transition
    // from MainScene to TitleScene is detected.
    // Including (from GdxApp):-
    //      - EntityData
    //      - AABBRenderer
    private static GameProgress gameProgress;
    private static Entities       entities;
    private static LevelManager   levelManager;
    private static HeadsUpDisplay hud;
    private static CollisionUtils collisionUtils;
    private static EntityManager  entityManager;
    private static EntityUtils    entityUtils;
    private static MapCreator     mapCreator;
    private static TiledUtils     tiledUtils;
    private static RoomManager    roomManager;

    // ------------------------------------------------
    // General globals
    public static boolean isStartupDone = false;

    // ------------------------------------------------
    // CODE
    // ------------------------------------------------

    public static void createObjects()
    {
        Trace.fileInfo();

        GdxApp.setup();

        // -----------------------
        // These are not initialised in GdxApp as these are local
        // classes implementing GdxUtils interfaces, or
        // extending GdxUtils classes..
        GdxApp.setSettings(new Settings());
        GdxApp.setAssets(new LocalAssetsData());
        GdxApp.setAudio(new GameAudio());

        // -----------------------
        inputManager = new InputManager();
        baseRenderer = new BaseRenderer();
        worldModel   = new WorldModel();

        // -----------------------
        gameProgress   = new GameProgress();
        entities       = new Entities();
        levelManager   = new LevelManager();
        hud            = new HeadsUpDisplay();
        collisionUtils = new CollisionUtils();
        entityManager  = new EntityManager();
        entityUtils    = new EntityUtils();
        mapCreator     = new MapCreator();
        tiledUtils     = new TiledUtils();
        roomManager    = new RoomManager();
    }

    public static void deleteObjects()
    {
        Trace.fileInfo();

        GdxApp.tidy();

        // -----------------------
        inputManager.dispose();
        baseRenderer.dispose();
        worldModel.dispose();

        inputManager = null;
        baseRenderer = null;
        worldModel   = null;

        // -----------------------
        entityManager.dispose();
        gameProgress.dispose();
        mapCreator.dispose();
        entities.dispose();

        AudioData.tidy();

        gameProgress   = null;
        entities       = null;
        levelManager   = null;
        hud            = null;
        collisionUtils = null;
        entityManager  = null;
        entityUtils    = null;
        mapCreator     = null;
        tiledUtils     = null;
        roomManager    = null;

        Trace.dbg("Done.");
    }

    // ------------------------------------------------
    //@formatter:off
    public static BaseRenderer      getBaseRenderer()       {   return baseRenderer;    }
    public static MainGame          getMainGame()           {   return mainGame;        }
    public static WorldModel        getWorldModel()         {   return worldModel;      }
    public static TitleScene        getTitleScene()         {   return titleScene;      }
    public static InputManager      getInputManager()       {   return inputManager;    }
    // ------------------------------------------------
    public static MainScene         getMainScene()          {   return mainScene;       }
    // ------------------------------------------------
    public static GameProgress      getGameProgress()       {  return gameProgress;     }
    public static Entities          getEntities()           {  return entities;         }
    public static LevelManager      getLevelManager()       {  return levelManager;     }
    public static HeadsUpDisplay    getHud()                {  return hud;              }
    public static CollisionUtils    getCollisionUtils()     {  return collisionUtils;   }
    public static EntityManager     getEntityManager()      {  return entityManager;    }
    public static EntityUtils       getEntityUtils()        {  return entityUtils;      }
    public static MapCreator        getMapCreator()         {  return mapCreator;       }
    public static TiledUtils        getTiledUtils()         {  return tiledUtils;       }
    public static RoomManager       getRoomManager()        {  return roomManager;      }
    //@formatter:on
    // ------------------------------------------------

    public static void setMainGame(MainGame game)
    {
        mainGame = game;
    }

    public static void setTitleScene(TitleScene scene)
    {
        titleScene = scene;
    }

    public static void setMainScene(MainScene scene)
    {
        mainScene = scene;
    }

    // ------------------------------------------------

    public static Paddle getPlayer()
    {
        return entities.paddle;
    }

    public static Ball getBall(int index)
    {
        return entities.balls.get(index);
    }

    public static BricksManager getBricksManager()
    {
        return (BricksManager) getEntityData().getManagerList().get(entityManager.bricksManagerIndex);
    }

    public static PickupManager getPickupManager()
    {
        return (PickupManager) getEntityData().getManagerList().get(entityManager.pickupManagerIndex);
    }

    public static BallManager getBallManager()
    {
        return (BallManager) getEntityData().getManagerList().get(entityManager.ballManagerIndex);
    }

    public static int getLevel()
    {
        return gameProgress.gameLevel;
    }
}
