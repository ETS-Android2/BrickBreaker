package com.richikin.gdxutils.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.richikin.gdxutils.assets.IAssets;
import com.richikin.gdxutils.audio.IAudioData;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.config.IApplicationConfig;
import com.richikin.gdxutils.entities.EntityData;
import com.richikin.gdxutils.google.IAdsController;
import com.richikin.gdxutils.google.IPlayServices;
import com.richikin.gdxutils.google.PlayServicesData;
import com.richikin.gdxutils.graphics.MapData;
import com.richikin.gdxutils.logging.StateManager;
import com.richikin.gdxutils.physics.aabb.AABBRenderer;

// ------------------------------------------------------------------
// Code
// ------------------------------------------------------------------

public class GdxApp
{
    // ---------------------------------------------
    private static ISettings          settings;
    private static IAdsController     adsController;
    private static IPlayServices      googleServices;
    private static IAssets            assets;
    private static IApplicationConfig appConfig;
    private static IAudioData         audio;
    // ---------------------------------------------
    private static PlayServicesData   playServicesData;
    private static HighScoreUtils     highScoreUtils;
    private static Stage              stage;
    // ---------------------------------------------
    private static StateManager       appState;
    private static SpriteBatch        spriteBatch;
    private static AABBRenderer       aabbRenderer;
    private static EntityData         entityData;
    private static MapData            mapData;

    // ------------------------------------------------
    // CODE
    // ------------------------------------------------

    public static void setup()
    {
        appState     = new StateManager(StateID._STATE_POWER_UP);
        spriteBatch  = new SpriteBatch();
        aabbRenderer = new AABBRenderer();
        mapData      = new MapData();
        entityData   = new EntityData();
    }

    // ------------------------------------------------
    //@formatter:off
    public static ISettings             getSettings()           {   return settings;            }
    public static IAdsController        getAdsController()      {   return adsController;       }
    public static IPlayServices         getPlayServices()       {   return googleServices;      }
    public static IAssets               getAssets()             {   return assets;              }
    public static IApplicationConfig    getAppConfig()          {   return appConfig;           }
    public static IAudioData            getAudio()              {   return audio;               }
    // ------------------------------------------------
    public static PlayServicesData      getPlayServicesData()   {   return playServicesData;    }
    public static HighScoreUtils        getHighScoreUtils()     {   return highScoreUtils;      }
    public static Stage                 getStage()              {   return stage;               }
    // ------------------------------------------------
    public static StateManager          getAppState()           {   return appState;            }
    public static SpriteBatch           getSpriteBatch()        {   return spriteBatch;         }
    public static AABBRenderer          getAABBRenderer()       {   return aabbRenderer;        }
    public static EntityData            getEntityData()         {   return entityData;          }
    public static MapData               getMapData()            {   return mapData;             }
    //@formatter:on
    // ------------------------------------------------

    public static void setPlayServices(IPlayServices services)
    {
        googleServices = services;
    }

    public static void setAdsController(IAdsController controller)
    {
        adsController = controller;
    }

    public static void setPlayServicesData(PlayServicesData psData)
    {
        playServicesData = psData;
    }

    public static void createStage(Viewport viewport)
    {
        stage = new Stage(viewport, getSpriteBatch());
    }

    public static void setSettings(ISettings _settings)
    {
        settings = _settings;
    }

    public static void setAssets(IAssets _assets)
    {
        assets = _assets;
    }

    public static void setAppConfig(IApplicationConfig config)
    {
        appConfig = config;
    }

    public static void setAudio(IAudioData _audio)
    {
        audio = _audio;
    }
    
    // ------------------------------------------------

    public static void tidy()
    {
        stage.dispose();
        spriteBatch.dispose();
        assets.dispose();
        settings.dispose();
        mapData.dispose();
        entityData.dispose();

        if (GdxSystem.inst().isAndroidApp())
        {
            googleServices.signOut();
        }

        mapData          = null;
        adsController    = null;
        googleServices   = null;
        playServicesData = null;
        spriteBatch      = null;
        stage            = null;
        appState         = null;
        assets           = null;
        settings         = null;
        audio            = null;
        appConfig        = null;
        highScoreUtils   = null;
        entityData       = null;
        aabbRenderer     = null;
    }
}
