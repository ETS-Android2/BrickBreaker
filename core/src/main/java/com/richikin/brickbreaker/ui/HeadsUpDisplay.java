package com.richikin.brickbreaker.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.core.GameConstants;
import com.richikin.brickbreaker.entities.Entities;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.brickbreaker.ui.panels.PanelManager;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.graphics.camera.OrthoGameCamera;
import com.richikin.gdxutils.graphics.text.FontUtils;
import com.richikin.gdxutils.input.Switch;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.ui.ProgressBar;

import java.util.Locale;

public class HeadsUpDisplay
{
    public static final int _VERY_LARGE_FONT_SIZE = 60;
    public static final int _LARGE_FONT_SIZE      = 40;
    public static final int _MID_FONT_SIZE        = 35;
    public static final int _SMALL_FONT_SIZE      = 25;

    private static final int _X = 0;
    private static final int _Y = 1;

    private static final int _SCORE  = 0;
    private static final int _LIVES  = 1;
    private static final int _LEVEL  = 2;
    private static final int _TIMER  = 3;
    private static final int _BORDER = 4;
    private static final int _PAUSE  = 5;

    private static final int _LIVES_SPACING = 42;

    public static final int _EXPANDED  = 0;
    public static final int _FASTBALL  = 1;
    public static final int _SLOWBALL  = 2;
    public static final int _MULTIBALL = 3;

    //@formatter:off
    public final int[][] displayPos =
        {
            //  X     Y
            {   64,   32},      // Score
            {  571,   63},      // Balls
            {  290,   40},      // Level
            {  221,  140},      // Timer
            {  215,  178},      // Border
            {  554,  160},      // Pause
        };
    //@formatter:on

    public float   hudOriginX;
    public float   hudOriginY;
    public StateID hudStateID;
    public Switch  buttonLeft;
    public Switch  buttonRight;
    public Switch  buttonPause;
    public boolean bonusClockActive;

    private Texture            scorePanel;
    private BitmapFont         smallFont;
    private BitmapFont         midFont;
    private BitmapFont         bigFont;
    private BitmapFont         veryBigFont;
    private TextMessageManager textMessageManager;
    private PanelManager       panelManager;
    private PausePanel         pausePanel;
    private TextureRegion[]    hudBalls;
    private ProgressBar        progressBar;
    private TextureRegion[]    borderFrames;
    private int                borderFrameIndex;

    // ----------------------------------------------------------------
    // Code
    // ----------------------------------------------------------------

    public HeadsUpDisplay()
    {
    }

    public void createHud()
    {
        Trace.fileInfo();

        createHudFonts();

        scorePanel = App.getAssets().loadSingleAsset(GameAssets._HUD_PANEL_ASSET, Texture.class);

        GameAssets.hudPanelWidth  = scorePanel.getWidth();
        GameAssets.hudPanelHeight = scorePanel.getHeight();

        createProgressBar();

        //
        // TODO: 03/12/2021
        // Both of these are currently WIP classes.
        textMessageManager = new TextMessageManager();
        panelManager       = new PanelManager();

        createHUDButtons();

        //
        // Load the images to be used for displaying
        // the number of lives
        hudBalls = new TextureRegion[GameConstants._MAX_LIVES];
        for (int i = 0; i < GameConstants._MAX_LIVES; i++)
        {
            hudBalls[i] = App.getAssets().getObjectRegion("hud_ball");
        }

        bonusClockActive    = false;
        hudStateID          = StateID._STATE_PANEL_START;
    }

    public void update()
    {
        switch (hudStateID)
        {
            case _STATE_PANEL_START:
            {
                hudStateID = StateID._STATE_PANEL_UPDATE;
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                if (buttonPause.isPressed())
                {
                    pausePanel = new PausePanel((int) hudOriginX, (int) hudOriginY);
                    pausePanel.setup();
                    App.getAppConfig().pause();
                    buttonPause.release();
                }

                textMessageManager.update();
                panelManager.update();

                //
                // Bonus Countdown timer. The certain bonuses will expire
                // when this timer reaches zero.
                // - G_EXPAND_BONUS
                // - G_SPEEDUP_BONUS
                // - G_SLOWDOWN_BONUS (VERY_SLOW_SPEED)
                if (bonusClockActive)
                {
                    progressBar.updateSlowDecrement();

                    if (progressBar.isEmpty())
                    {
                        bonusClockActive = false;

                        if (App.getEntities().ballsAreFast() || App.getEntities().ballsAreVerySlow())
                        {
                            App.getEntities().setBallSpeed(Entities._SLOW_BALL_SPEED);
                        }
                        else if (App.getPlayer().isStretched)
                        {
                            App.getPlayer().setExpanded(false);
                        }

                        progressBar.refill();
                    }
                }
            }
            break;

            case _STATE_DEVELOPER_PANEL:
            {
                // TODO: 18/11/2021
            }
            break;

            case _STATE_PAUSED:
            {
                pausePanel.update();

                if (buttonPause.isPressed())
                {
                    pausePanel.dispose();
                    App.getAppConfig().unPause();
                    buttonPause.release();
                }
            }
            break;

            default:
                break;
        }
    }

    public void render(OrthoGameCamera camera, boolean canDrawControls)
    {
        hudOriginX = camera.getPosition().x - (GfxData._HUD_WIDTH / 2f);
        hudOriginY = camera.getPosition().y - (GfxData._HUD_HEIGHT / 2f);

        drawPanels();
        drawItems();
        drawMessages();

        if (hudStateID == StateID._STATE_PAUSED)
        {
            pausePanel.draw(App.getSpriteBatch());
        }

        drawHudDebug();
    }

    public void setStateID(StateID state)
    {
        hudStateID = state;
    }

    public void refillItems()
    {
    }

    public void setBorderFrameIndex(int borderFrameIndex)
    {
        this.borderFrameIndex = borderFrameIndex;
    }

    public PanelManager getPanelManager()
    {
        return panelManager;
    }

    private void drawPanels()
    {
        App.getSpriteBatch().draw(scorePanel, hudOriginX, hudOriginY);

        if (bonusClockActive)
        {
            try
            {
                App.getSpriteBatch().draw
                        (
                                borderFrames[borderFrameIndex],
                                (int) hudOriginX + displayPos[_BORDER][_X],
                                (int) hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_BORDER][_Y])
                        );
            }
            catch (NullPointerException npe)
            {
                Trace.info("borderFramesIndex = ", borderFrameIndex);
            }
        }
    }

    private void drawItems()
    {
        bigFont.draw
            (
                App.getSpriteBatch(),
                String.format(Locale.UK, "%d", App.getGameProgress().score.getTotal()),
                hudOriginX + displayPos[_SCORE][_X],
                hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_SCORE][_Y])
            );

        bigFont.draw
            (
                App.getSpriteBatch(),
                String.format(Locale.UK, "%03d", App.getGameProgress().gameLevel),
                hudOriginX + displayPos[_LEVEL][_X],
                hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_LEVEL][_Y])
            );

        if (bonusClockActive)
        {
            progressBar.draw
                (
                    (int) hudOriginX + displayPos[_TIMER][_X],
                    (int) hudOriginY + (Gfx._HUD_HEIGHT - displayPos[_TIMER][_Y]),
                    App.getSpriteBatch()
                );
        }

        for (int i = 0; i < GameConstants._MAX_LIVES; i++)
        {
            if (i < App.getGameProgress().lives.getTotal())
            {
                App.getSpriteBatch().draw
                    (
                        hudBalls[i],
                        displayPos[_LIVES][_X] - (i * _LIVES_SPACING),
                        (Gfx._HUD_HEIGHT - displayPos[_LIVES][_Y])
                    );
            }
        }
    }

    private void drawMessages()
    {
        if (!GdxSystem.inst().gamePaused)
        {
            if (panelManager.isEnabled())
            {
                panelManager.draw();
            }
        }
    }

    public void showControls()
    {
    }

    public void showPauseButton(boolean b)
    {
    }

    private void createProgressBar()
    {
        TextureRegion region = App.getAssets().getObjectRegion("progressbar_border");
        borderFrames = new TextureRegion[4];

        TextureRegion[][] tmpFrames = region.split(222, 64);

        int i = 0;

        for (TextureRegion[] frame : tmpFrames)
        {
            for (TextureRegion textureRegion : frame)
            {
                if (i < 4)
                {
                    borderFrames[i++] = textureRegion;
                }
            }
        }

        borderFrameIndex = 0;

        Trace.dbg("borderFrames[] length = ", borderFrames.length);

        progressBar = new ProgressBar(7, 210, 210, "bar9");
        progressBar.setHeightColorScale(20, Color.RED, 1.0f);
        progressBar.setSubInterval(1000);
    }

    private void createHudFonts()
    {
        FontUtils fontUtils = new FontUtils();

        veryBigFont = fontUtils.createFont(GameAssets._ACME_REGULAR_FONT, _VERY_LARGE_FONT_SIZE);
        bigFont     = fontUtils.createFont(GameAssets._ACME_REGULAR_FONT, _LARGE_FONT_SIZE);
        midFont     = fontUtils.createFont(GameAssets._ACME_REGULAR_FONT, _MID_FONT_SIZE);
        smallFont   = fontUtils.createFont(GameAssets._ACME_REGULAR_FONT, _SMALL_FONT_SIZE);
    }

    /**
     * Creates any buttons used for the HUD.
     * For this game, the lower left and right
     * halfs of the screen will be used for
     * movement 'buttons'.
     */
    private void createHUDButtons()
    {
        buttonLeft  = new Switch();
        buttonRight = new Switch();
        buttonPause = new Switch();
    }

    private void drawHudDebug()
    {
//        if (App.getSettings().isEnabled(Settings._SHOW_DEBUG))
//        {
//        smallFont.setColor(Color.WHITE);
//
//        smallFont.draw
//            (
//                App.getSpriteBatch(),
//                "Active Bricks: " + App.getBricksManager().getActiveCount() + "(" + App.getEntityData().getEntityMap().size+")",
//                hudOriginX + 30,
//                hudOriginY + (992 - 150)
//            );
//
//        smallFont.draw
//            (
//                App.getSpriteBatch(),
//                "Active Pickups: " + App.getPickupManager().getActiveCount(),
//                hudOriginX + 30,
//                hudOriginY + (992 - 180)
//            );
//
//        smallFont.draw
//            (
//                App.getSpriteBatch(),
//                "Active Balls  : " + App.getEntities().balls.size + "  Speed: " + App.getEntities().getBallSpeed(),
//                hudOriginX + 30,
//                hudOriginY + (992 - 210)
//            );
//
//        smallFont.draw
//            (
//                App.getSpriteBatch(),
//                "AppState  : " + App.getAppState().peek(),
//                hudOriginX + 30,
//                hudOriginY + 30
//            );
//
//            buttonLeft.draw();
//            buttonRight.draw();
//
//            // ---------------------------------------------
//
//            if (App.getAppConfig().isGodMode())
//            {
//                smallFont.draw(App.getSpriteBatch(), "GOD MODE", hudOriginX + 10, hudOriginY + (992 - 80));
//            }
//
//            smallFont.draw(App.getSpriteBatch(), "DEV MODE", hudOriginX + 10, hudOriginY + (992 - 100));
//
//            // ---------------------------------------------
//
//            int yPosition = 992 - 130;
//
//            if (App.getSettings().isEnabled(Settings._SHOW_FPS))
//            {
//                smallFont.draw
//                    (
//                        App.getSpriteBatch(),
//                        "FPS  : " + Gdx.graphics.getFramesPerSecond(),
//                        hudOriginX + 10,
//                        hudOriginY + yPosition
//                    );
//
//                smallFont.draw
//                    (
//                        App.getSpriteBatch(),
//                        "BRICKS : " + App.getBricksManager().getActiveCount(),
//                        hudOriginX + 400,
//                        hudOriginY + yPosition
//                    );
//
//                yPosition -= 30;
//
//                smallFont.draw
//                    (
//                        App.getSpriteBatch(),
//                        "BALL SPD : " + App.getEntities().getBallSpeed(),
//                        hudOriginX + 400,
//                        hudOriginY + yPosition
//                    );
//
//                yPosition -= 30;
//            }
//
//            // ---------------------------------------------
//        }
    }
}
