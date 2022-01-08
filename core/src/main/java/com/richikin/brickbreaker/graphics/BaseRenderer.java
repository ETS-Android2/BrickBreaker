package com.richikin.brickbreaker.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.graphics.camera.OrthoGameCamera;
import com.richikin.gdxutils.graphics.camera.ViewportType;
import com.richikin.gdxutils.graphics.camera.Zoom;
import com.richikin.gdxutils.graphics.effects.StarField;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec3F;

public class BaseRenderer implements Disposable
{
    public OrthoGameCamera hudGameCamera;
    public OrthoGameCamera mainGameCamera;
    public OrthoGameCamera backgroundCamera;
    public SimpleVec3F     cameraPos;
    public Zoom            gameZoom;
    public Zoom            hudZoom;
    public boolean         isDrawingStage;
    public Texture         backgroundImage;

    private WorldRenderer worldRenderer;
    private HUDRenderer   hudRenderer;
    private StarField     starField;

    public BaseRenderer()
    {
    }

    /**
     * Create all game cameras and
     * associated viewports.
     */
    public void createCameras()
    {
        Trace.fileInfo();

        GdxSystem.inst().camerasReady = false;

        // --------------------------------------
        // Camera for displaying a background behind the game sprites.
        // Using a seperate camera to allow camera effects to be applied
        // to mainGameCamera without affecting the background.
        backgroundCamera = new OrthoGameCamera
            (
                GfxData._GAME_SCENE_WIDTH, GfxData._GAME_SCENE_HEIGHT,
                ViewportType._EXTENDED,
                "Background Cam"
            );

        // --------------------------------------
        // Camera for displaying game scene, usually just sprites.
        // If the game uses Tiled Maps then this one camera will be joined by
        // a new tiledMapCamera.
        // If the game uses Parallax scrolling backgrounds then a new
        // parallaxGameCamera will be added.
        // If either of the above cameras are added, they should be updated
        // BEFORE mainGameCamera.
        mainGameCamera = new OrthoGameCamera
            (
                GfxData._GAME_SCENE_WIDTH, GfxData._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Main Cam"
            );

        // --------------------------------------
        // Camera for displaying the HUD
        // Using a seperate camera to allow camera effects to be applied
        // to mainGameCamera without affecting the hud.
        hudGameCamera = new OrthoGameCamera
            (
                GfxData._HUD_SCENE_WIDTH, GfxData._HUD_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Hud Cam"
            );

        gameZoom      = new Zoom();
        hudZoom       = new Zoom();
        cameraPos     = new SimpleVec3F();
        worldRenderer = new WorldRenderer();
        hudRenderer   = new HUDRenderer();
        starField     = new StarField();

        isDrawingStage                = false;
        GdxSystem.inst().camerasReady = true;
    }

    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        if (AppConfig.gameScreenActive())
//        {
//        }
//        else
        {
            App.getMapData().mapPosition.set(0, 0);
        }

        App.getSpriteBatch().enableBlending();

        drawBackgroundImage();
        updateMainSceneCamera();
        updateHudCamera();

        // ----- Draw the Stage, if enabled -----
        if (isDrawingStage && (App.getStage() != null))
        {
            App.getStage().act(Math.min(Gdx.graphics.getDeltaTime(), GfxData._STEP_TIME));
            App.getStage().draw();
        }

        gameZoom.stop();
        hudZoom.stop();

        App.getWorldModel().drawDebugMatrix();
    }

    public void resizeCameras(int width, int height)
    {
        backgroundCamera.resizeViewport(width, height, true);
        mainGameCamera.resizeViewport(width, height, true);
        hudGameCamera.resizeViewport(width, height, true);
    }

    public void resetCameraZoom()
    {
        backgroundCamera.camera.update();
        mainGameCamera.camera.update();
        hudGameCamera.camera.update();

        gameZoom.stop();
        hudZoom.stop();

        backgroundCamera.camera.zoom = 1.0f;
        mainGameCamera.camera.zoom   = 1.0f;
        hudGameCamera.camera.zoom    = 1.0f;
    }

    public void enableAllCameras()
    {
        backgroundCamera.isInUse = true;
        mainGameCamera.isInUse   = true;
        hudGameCamera.isInUse    = true;
        isDrawingStage           = true;
    }

    public void disableAllCameras()
    {
        backgroundCamera.isInUse = false;
        mainGameCamera.isInUse   = false;
        hudGameCamera.isInUse    = false;
        isDrawingStage           = false;
    }

    private void drawBackgroundImage()
    {
        if (backgroundCamera.isInUse)
        {
            backgroundCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix(backgroundCamera.camera.combined);
            App.getSpriteBatch().begin();

            cameraPos.setEmpty();
            backgroundCamera.setPosition(cameraPos);

            if (backgroundImage != null)
            {
                App.getSpriteBatch().draw
                    (
                        backgroundImage,
                        cameraPos.x - (backgroundCamera.camera.viewportWidth / 2),
                        cameraPos.y - (backgroundCamera.camera.viewportHeight / 2),
                        backgroundCamera.camera.viewportWidth,
                        backgroundCamera.camera.viewportHeight
                    );
            }

            starField.render();

            App.getSpriteBatch().end();
        }
    }

    private void updateMainSceneCamera()
    {
        if (mainGameCamera.isInUse)
        {
            mainGameCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix(mainGameCamera.camera.combined);
            App.getSpriteBatch().begin();

            cameraPos.x = (App.getMapData().mapPosition.getX() + (mainGameCamera.camera.viewportWidth / 2));
            cameraPos.y = (App.getMapData().mapPosition.getY() + (mainGameCamera.camera.viewportHeight / 2));
            cameraPos.z = 0;

            mainGameCamera.setPosition(cameraPos, gameZoom.getZoomValue(), false);

            App.getMapCreator().render(mainGameCamera.camera);

            worldRenderer.render(App.getSpriteBatch(), mainGameCamera);

            App.getSpriteBatch().end();
        }
    }

    private void updateHudCamera()
    {
        if (hudGameCamera.isInUse)
        {
            hudGameCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix(hudGameCamera.camera.combined);
            App.getSpriteBatch().begin();

            cameraPos.x = (hudGameCamera.camera.viewportWidth / 2);
            cameraPos.y = (hudGameCamera.camera.viewportHeight / 2);
            cameraPos.z = 0;

            hudGameCamera.setPosition(cameraPos, hudZoom.getZoomValue(), false);

            hudRenderer.render(App.getSpriteBatch(), hudGameCamera);

            App.getSpriteBatch().end();
        }
    }

    @Override
    public void dispose()
    {
        backgroundCamera.dispose();
        mainGameCamera.dispose();
        hudGameCamera.dispose();

        backgroundCamera = null;
        mainGameCamera   = null;
        hudGameCamera    = null;

        backgroundImage = null;
        cameraPos       = null;
        gameZoom        = null;
        hudZoom         = null;
        worldRenderer   = null;
        hudRenderer     = null;
        starField       = null;
    }
}
