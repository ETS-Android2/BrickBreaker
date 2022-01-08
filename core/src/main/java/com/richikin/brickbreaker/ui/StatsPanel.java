package com.richikin.brickbreaker.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.logging.SystemMeters;
import com.richikin.gdxutils.graphics.text.FontUtils;
import com.richikin.gdxutils.logging.Stats;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.ui.DefaultPanel;
import com.richikin.gdxutils.ui.Scene2DUtils;

public class StatsPanel extends DefaultPanel
{
    private static class StatsInfo
    {
        final String       name;
        final SystemMeters meter;

        StatsInfo(String _name, SystemMeters _meter)
        {
            this.name  = _name;
            this.meter = _meter;
        }
    }

    //@formatter:off
    private final StatsInfo[] systemMeterNames =
        {
            new StatsInfo("  Illegal Game Mode            ", SystemMeters._ILLEGAL_GAME_MODE),
            new StatsInfo("  Sound Load Failure           ", SystemMeters._SOUND_LOAD_FAIL),
            new StatsInfo("  Bad Player Action            ", SystemMeters._BAD_PLAYER_ACTION),
            new StatsInfo("  Font Load Failure            ", SystemMeters._FONT_LOAD_FAILURE),
            new StatsInfo("  Bordered Font Load Failure   ", SystemMeters._BORDERED_FONT_LOAD_FAILURE),

            new StatsInfo("divider", null),

            new StatsInfo("  I/O Exception                ", SystemMeters._IO_EXCEPTION),
            new StatsInfo("  Index Out Of Bounds Exception", SystemMeters._INDEX_OUT_OF_BOUNDS_EXCEPTION),
            new StatsInfo("  Array Index O.O.B Exception  ", SystemMeters._ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION),
            new StatsInfo("  SAX Exception                ", SystemMeters._SAX_EXCEPTION),
            new StatsInfo("  Interrupted Exception        ", SystemMeters._INTERRUPTED_EXCEPTION),
            new StatsInfo("  Null Pointer Exception       ", SystemMeters._NULL_POINTER_EXCEPTION),
            new StatsInfo("  Illegal State Exception      ", SystemMeters._ILLEGAL_STATE_EXCEPTION),
            new StatsInfo("  GDX Runtime Exception        ", SystemMeters._GDX_RUNTIME_EXCEPTION),
            new StatsInfo("  Unknown Exception            ", SystemMeters._UNKNOWN_EXCEPTION),
            new StatsInfo("  Should Always be ZERO >>>>   ", SystemMeters._DUMMY_METER),
            new StatsInfo("  Entity Data Exception        ", SystemMeters._ENTITY_DATA_EXCEPTION),

            new StatsInfo("divider", null),
        };
    //@formatter:on

    private       TextButton title;
    private final int        originX;
    private final int originY;

    public StatsPanel(int x, int y)
    {
        nameID  = "Stats Panel";
        originX = x;
        originY = y;
    }


    @Override
    public void setup()
    {
        Trace.fileInfo();

        skin = new Skin(Gdx.files.internal(App.getAssets().getSkinFilename()));

        table = new Table();
        table.top().left();
        table.pad(30, 30, 30, 30);
        table.setDebug(false);

        populateTable();

        // Wrap the buffer in a scrollpane.
        Scene2DUtils utils = new Scene2DUtils();

        scrollPane = utils.createScrollPane(table, skin, "");
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._HUD_WIDTH - 80);
        scrollPane.setHeight((float) (Gfx._HUD_HEIGHT / 8) * 6);
        scrollPane.setPosition((originX + 40), (originY + 120));

        App.getStage().addActor(scrollPane);
    }

    @Override
    public void populateTable()
    {
        Trace.fileInfo();

        try
        {
            FontUtils fontUtils = new FontUtils();

            title = new TextButton("STATS METERS", skin);
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(title.getStyle());
            style.font = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 24, Color.WHITE);
            style.fontColor = Color.WHITE;

            title.setStyle(style);
            title.setSize(300, 40);
            title.setPosition(100, 880);
            title.setDisabled(true);

            App.getStage().addActor(title);

            TextField.TextFieldStyle textFieldStyle;

            for (StatsInfo meterName : systemMeterNames)
            {
                if ("divider".equals(meterName.name))
                {
                    TextureRegion         region   = App.getAssets().getObjectRegion("divider");
                    TextureRegionDrawable drawable = new TextureRegionDrawable(region);
                    Image                 image    = new Image(drawable);

                    table.add(image).padLeft(200);
                    table.row();
                }
                else
                {
                    TextField label = new TextField(meterName.name, skin);
                    textFieldStyle           = new TextField.TextFieldStyle(label.getStyle());
                    textFieldStyle.font      = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 18, Color.WHITE);
                    textFieldStyle.fontColor = Color.WHITE;

                    label.setStyle(textFieldStyle);
                    label.setAlignment(Align.left);
                    label.setDisabled(true);

                    String meterString = Integer.toString(Stats.getMeter(meterName.meter.get()));

                    TextField meterLabel = new TextField(meterString, skin);
                    textFieldStyle           = new TextField.TextFieldStyle(meterLabel.getStyle());
                    textFieldStyle.font      = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 18, Color.YELLOW);
                    textFieldStyle.fontColor = Color.YELLOW;

                    meterLabel.setStyle(textFieldStyle);
                    meterLabel.setAlignment(Align.center);
                    meterLabel.setDisabled(true);

                    float prefHeight = label.getPrefHeight() * 1.5f;

                    table.add(label).padLeft(10).padBottom(10).prefWidth((float) Gfx._HUD_WIDTH / 3).prefHeight(prefHeight);
                    table.add(meterLabel).padLeft(10).padBottom(10).prefWidth((float) Gfx._HUD_WIDTH / 4).prefHeight(prefHeight);
                }

                table.row();
            }

            table.setVisible(true);
        }
        catch (NullPointerException npe)
        {
            Trace.fileInfo();
            Stats.incMeter(SystemMeters._NULL_POINTER_EXCEPTION.get());
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
    }

    @Override
    public void dispose()
    {
        textureRegion = null;
        nameID        = null;

        table.clear();
        table.addAction(Actions.removeActor());
        table = null;

        scrollPane.clear();
        scrollPane.addAction(Actions.removeActor());
        scrollPane = null;

        title.addAction(Actions.removeActor());
        title = null;
    }
}
