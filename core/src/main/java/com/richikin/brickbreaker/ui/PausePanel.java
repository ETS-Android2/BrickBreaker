package com.richikin.brickbreaker.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.config.StandardSettings;
import com.richikin.gdxutils.ui.DefaultPanel;
import com.richikin.gdxutils.ui.Scene2DUtils;

public class PausePanel extends DefaultPanel
{
    private static final int _MUSIC          = 0;
    private static final int _SOUNDS         = 1;
    private static final int _NUM_CHECKBOXES = 2;

    private final int[][] displayPos =
        {
            {424, 147},
            {424,  95},
        };

    private Image       foreground;
    private CheckBox[]  checkBoxes;
    private ImageButton button;

    private int originX;
    private int originY;

    public PausePanel(int x, int y)
    {
        super();

        nameID  = "Pause Panel";
        originX = x;
        originY = y;
    }

    @Override
    public void setup()
    {
        Texture       texture = App.getAssets().loadSingleAsset(GameAssets._PAUSE_PANEL_ASSET, Texture.class);
        TextureRegion region  = new TextureRegion(texture);
        foreground = new Image(region);

        originX += ((Gfx._HUD_WIDTH - foreground.getPrefWidth()) / 2);
        originY += ((Gfx._HUD_HEIGHT - foreground.getPrefHeight()) / 2);

        foreground.setPosition(originX, originY);
        App.getStage().addActor(foreground);

        String skinFilename = GdxApp.getAssets().getSkinFilename();

        if (skinFilename.equals(""))
        {
            skin = new Skin();
        }
        else
        {
            skin = new Skin(Gdx.files.internal(GdxApp.getAssets().getSkinFilename()));
        }

        createButtons();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
    }

    private void createButtons()
    {
        checkBoxes = new CheckBox[_NUM_CHECKBOXES];

        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.checkboxOn  = new TextureRegionDrawable(GdxApp.getAssets().getButtonRegion("toggle_on"));
        style.checkboxOff = new TextureRegionDrawable(GdxApp.getAssets().getButtonRegion("toggle_off"));
        style.font = new BitmapFont();

        for (int i = 0; i < _NUM_CHECKBOXES; i++)
        {
            checkBoxes[i] = new CheckBox("", skin);
            checkBoxes[i].setStyle(style);
            checkBoxes[i].setPosition((originX + displayPos[i][0]), (originY + displayPos[i][1]));
            App.getStage().addActor(checkBoxes[i]);
        }

        checkBoxes[_MUSIC].setChecked(App.getSettings().isEnabled(StandardSettings._MUSIC_ENABLED));
        checkBoxes[_SOUNDS].setChecked(App.getSettings().isEnabled(StandardSettings._SOUNDS_ENABLED));

        Scene2DUtils utils = new Scene2DUtils();

        button = utils.addButton
            (
                "blank_button",
                "blank_button_pressed",
                (originX + 150),
                (originY + 33)
            );
        button.setChecked(false);
    }

    @Override
    public void dispose()
    {
        foreground.addAction(Actions.removeActor());
        foreground = null;

        for (int i = 0; i < _NUM_CHECKBOXES; i++)
        {
            checkBoxes[i].addAction(Actions.removeActor());
            checkBoxes[i] = null;
        }
        checkBoxes = null;

        button.addAction(Actions.removeActor());
        button = null;

        super.dispose();
    }
}
