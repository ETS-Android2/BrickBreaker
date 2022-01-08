package com.richikin.brickbreaker.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.graphics.camera.OrthoGameCamera;

public class WorldRenderer
{
    public WorldRenderer()
    {
    }

    public void render(SpriteBatch spriteBatch, OrthoGameCamera gameCamera)
    {
        if (!GdxSystem.inst().shutDownActive)
        {
            switch (App.getAppState().peek())
            {
                case _STATE_SETUP:
                case _STATE_GET_READY:
                case _STATE_PAUSED:
                case _STATE_LEVEL_RETRY:
                case _STATE_PREPARE_LEVEL_FINISHED:
                case _STATE_LEVEL_FINISHED:
                case _STATE_DEV_RESTART_LEVEL1:
                case _STATE_GAME:
                case _STATE_SETTINGS_PANEL:
                case _STATE_DEBUG_HANG:
                {
                    if (App.getMainScene() != null)
                    {
                        App.getMainScene().draw(spriteBatch, gameCamera);
                    }

                    if (!App.getAppConfig().isUsingAshleyECS())
                    {
                        App.getEntityManager().drawSprites();
                    }

                    if (!App.getAppConfig().isUsingBox2DPhysics())
                    {
                        GdxApp.getAABBRenderer().drawBoxes();
                    }
                }
                break;

                case _STATE_MAIN_MENU:
                case _STATE_CLOSING:
                case _STATE_GAME_OVER:
                case _STATE_END_GAME:
                default:
                    break;
            }
        }
    }
}
