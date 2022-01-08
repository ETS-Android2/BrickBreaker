package com.richikin.brickbreaker.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.TimeUtils;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.ui.Scene2DUtils;

import java.util.Calendar;
import java.util.Date;

public class MenuPage implements IUIPage
{
    public ImageButton buttonStart;
    public ImageButton buttonOptions;
    public ImageButton buttonExit;

    private Image decoration;

    public MenuPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.fileInfo();

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        buttonStart   = scene2DUtils.addButton("buttonStart", "buttonStart_pressed", 164, (Gfx._HUD_HEIGHT - 590));
        buttonOptions = scene2DUtils.addButton("buttonOptions", "buttonOptions_pressed", 217, (Gfx._HUD_HEIGHT - 680));
        buttonExit    = scene2DUtils.addButton("buttonExit", "buttonExit_pressed", 269, (Gfx._HUD_HEIGHT - 760));

        addDateSpecificItems(false);
    }

    @Override
    public boolean update()
    {
        return false;
    }

    @Override
    public void show()
    {
        Trace.fileInfo();

        showItems(true);

        GdxSystem.inst().hideAndDisableBackButton();
    }

    @Override
    public void hide()
    {
        Trace.fileInfo();

        showItems(false);
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
    }

    @SuppressWarnings("SameParameterValue")
    private void addDateSpecificItems(boolean force)
    {
        // TODO: 29/12/2020 - Add New Years Day, Mother Goddess Day

        Date     date     = new Date(TimeUtils.millis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        if ((calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) || force)
        {
            if ((calendar.get(Calendar.DAY_OF_MONTH) == 11) || force)
            {
                decoration = scene2DUtils.makeObjectsImage("poppy");
                decoration.setPosition(512, (Gfx._HUD_HEIGHT - 120));
                App.getStage().addActor(decoration);
            }
        }

        if ((calendar.get(Calendar.MONTH) == Calendar.DECEMBER) || force)
        {
            if ((calendar.get(Calendar.DAY_OF_MONTH) == 25) || force)
            {
                decoration = scene2DUtils.makeObjectsImage("xmas_tree");
                decoration.setPosition(0, 0);
                App.getStage().addActor(decoration);
            }
        }
    }

    private void showItems(boolean visible)
    {
        if (buttonStart != null)
        {
            buttonStart.setVisible(visible);
        }
        if (buttonOptions != null)
        {
            buttonOptions.setVisible(visible);
        }
        if (buttonExit != null)
        {
            buttonExit.setVisible(visible);
        }
    }

    @Override
    public void dispose()
    {
        if (buttonStart != null)
        {
            buttonStart.addAction(Actions.removeActor());
        }
        if (buttonOptions != null)
        {
            buttonOptions.addAction(Actions.removeActor());
        }
        if (buttonExit != null)
        {
            buttonExit.addAction(Actions.removeActor());
        }

        if (decoration != null)
        {
            decoration.addAction(Actions.removeActor());
        }

        buttonStart   = null;
        buttonOptions = null;
        buttonExit    = null;
        decoration    = null;
    }
}
