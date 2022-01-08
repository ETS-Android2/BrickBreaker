package com.richikin.brickbreaker.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.graphics.camera.OrthoGameCamera;
import com.richikin.gdxutils.input.controllers.ControllerType;

public class HUDRenderer
{
    public HUDRenderer()
    {
    }

    public void render(SpriteBatch spriteBatch, OrthoGameCamera hudCamera)
    {
        if (!GdxSystem.inst().shutDownActive)
        {
            switch (App.getAppState().peek())
            {
                case _STATE_MAIN_MENU:
                {
                    if (App.getTitleScene() != null)
                    {
                        App.getTitleScene().draw(spriteBatch, hudCamera);
                    }
                }
                break;

                case _STATE_SETUP:
                case _STATE_GET_READY:
                case _STATE_PAUSED:
                case _STATE_PREPARE_LEVEL_FINISHED:
                case _STATE_LEVEL_FINISHED:
                case _STATE_LEVEL_RETRY:
                case _STATE_DEV_RESTART_LEVEL1:
                case _STATE_GAME:
                case _STATE_MESSAGE_PANEL:
                case _STATE_DEBUG_HANG:
                case _STATE_GAME_OVER:
                {
                    if (App.getHud() != null)
                    {
                        App.getHud().render(hudCamera, (GdxSystem.inst().availableInputs.contains(ControllerType._VIRTUAL, true)));
                    }
                }
                break;

                case _STATE_GAME_FINISHED:
                {
                    if (App.getHud() != null)
                    {
                        App.getHud().render(hudCamera, false);
                    }
                }
                break;

                case _STATE_CLOSING:
                default:
                {
                }
                break;
            }
        }
    }
}
