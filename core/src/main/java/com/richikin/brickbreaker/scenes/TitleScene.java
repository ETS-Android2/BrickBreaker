package com.richikin.brickbreaker.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.richikin.brickbreaker.audio.AudioData;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.brickbreaker.ui.CreditsPage;
import com.richikin.brickbreaker.ui.IUIPage;
import com.richikin.brickbreaker.ui.MenuPage;
import com.richikin.brickbreaker.ui.OptionsPage;
import com.richikin.gdxutils.core.ScreenID;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.graphics.camera.OrthoGameCamera;
import com.richikin.gdxutils.graphics.text.FontUtils;
import com.richikin.gdxutils.logging.Trace;

import java.util.ArrayList;

public class TitleScene extends AbstractBaseScene
{
    private static final int _MENU_PAGE    = 0;
    private static final int _OPTIONS_PAGE = 1;
    private static final int _CREDITS_PAGE = 2;
    private static final int _EXIT_PAGE    = 3;

    private Texture            foreground;
    private OptionsPage        optionsPage;
    private MenuPage           menuPage;
    private ArrayList<IUIPage> panels;
    private int                currentPage;
    private Dialog             exitDialog;
    private boolean            justLeftExitDialog;

    public TitleScene()
    {
        super();
    }

    @Override
    public void initialise()
    {
        optionsPage = new OptionsPage();
        menuPage    = new MenuPage();
        panels      = new ArrayList<>();
        currentPage = _MENU_PAGE;

        panels.add(_MENU_PAGE, menuPage);
        panels.add(_OPTIONS_PAGE, optionsPage);
        panels.add(_CREDITS_PAGE, new CreditsPage());

        menuPage.initialise();
        menuPage.show();
    }

    @Override
    public void update()
    {
        if (!App.getAudio().isTunePlaying(AudioData.MUS_TITLE))
        {
            App.getAudio().playTitleTune(true);
        }

        if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
        {
            switch (currentPage)
            {
                case _CREDITS_PAGE:
                {
                    panels.get(currentPage).update();

                    if (GdxSystem.inst().backButton != null)
                    {
                        if (GdxSystem.inst().backButton.isChecked())
                        {
                            GdxSystem.inst().backButton.setChecked(false);
                            changePageTo(_MENU_PAGE);
                        }
                    }
                }
                break;

                case _MENU_PAGE:
                {
                    panels.get(_MENU_PAGE).update();
                }
                break;

                case _OPTIONS_PAGE:
                {
                    if (panels.get(_OPTIONS_PAGE).update())
                    {
                        if (GdxSystem.inst().backButton != null)
                        {
                            GdxSystem.inst().backButton.setChecked(false);
                        }

                        changePageTo(_MENU_PAGE);
                    }
                }
                break;

                case _EXIT_PAGE:
                {
                    if (GdxSystem.inst().shutDownActive)
                    {
                        App.deleteObjects();
                        GdxSystem.inst().exit();
                    }
                    else
                    {
                        if (justLeftExitDialog)
                        {
                            changePageTo(_MENU_PAGE);
                        }
                    }
                }
                break;

                default:
                {
                    Trace.dbg("ERROR: Unknown page - " + currentPage);
                }
                break;
            }

            //
            // Start button check
            if ((menuPage.buttonStart != null) && menuPage.buttonStart.isChecked())
            {
                Trace.divider('#', 100);
                Trace.dbg(" ***** START PRESSED ***** ");
                Trace.divider('#', 100);

                App.getAudio().playTitleTune(false);

                menuPage.buttonStart.setChecked(false);

                //
                // This check is necessary because mainScene may have
                // been created due to titleScene being disabled by
                // Developer Options.
                if (App.getMainScene() == null)
                {
                    App.setMainScene(new MainScene());
                }

                App.getMainScene().reset();
                App.getMainGame().setScreen(App.getMainScene());
            }
            else
            {
                if (App.getAppState() != null)
                {
                    // If we're still on the title screen...
                    if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
                    {
                        //
                        // Check OPTIONS button, open settings page if pressed
                        if ((menuPage.buttonOptions != null) && menuPage.buttonOptions.isChecked())
                        {
                            menuPage.buttonOptions.setChecked(false);
                            changePageTo(_OPTIONS_PAGE);
                        }

                        //
                        // Check EXIT button, open exit dialog if pressed
                        if ((menuPage.buttonExit != null) && menuPage.buttonExit.isChecked())
                        {
                            menuPage.buttonExit.setChecked(false);
                            panels.get(currentPage).hide();

                            justLeftExitDialog = false;

                            changePageTo(_EXIT_PAGE);

                            ExitPanel();
                        }
                    }
                }
            }
        }
        else
        {
            Trace.dbg("Unsupported game state: " + App.getAppState().peek());
        }
    }

    private void ExitPanel()
    {
        FontUtils fontUtils = new FontUtils();

        Skin skin = new Skin(Gdx.files.internal(App.getAssets().getSkinFilename()));

        justLeftExitDialog = false;

        exitDialog = new Dialog("", skin)
        {
            protected void result(Object object)
            {
                GdxSystem.inst().shutDownActive = (Boolean) object;

                justLeftExitDialog = true;
            }
        };

        TextButton.TextButtonStyle buttonStyle  = new TextButton.TextButtonStyle();
        Label.LabelStyle labelStyle             = new Label.LabelStyle();

        buttonStyle.font        = fontUtils.createFont(GameAssets._ACME_REGULAR_FONT, 32);
        buttonStyle.fontColor   = Color.WHITE;
        labelStyle.font         = buttonStyle.font;
        labelStyle.fontColor    = buttonStyle.fontColor;

        Button buttonYes = new TextButton("YES", buttonStyle);
        Button buttonNo  = new TextButton("NO", buttonStyle);

        buttonYes.padRight(30);
        buttonNo.padLeft(30);

        exitDialog.button(buttonYes, true);
        exitDialog.button(buttonNo, false);
        exitDialog.text("Quit Game (Y/N)", labelStyle);
        exitDialog.key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
        exitDialog.setSize(300.0f, 200.0f);
        exitDialog.setPosition(Gfx._HUD_WIDTH / 2.0f, Gfx._HUD_HEIGHT / 2.0f, Align.center);

        App.getStage().addActor(exitDialog);
    }

    /**
     * Closes down the current page, and
     * switches to a new one.
     * Copy of LibGdx setScreen() method.
     *
     * @param nextPage The ID of the next page.
     */
    private void changePageTo(int nextPage)
    {
        Trace.fileInfo();
        Trace.info("currentPage: ", currentPage);
        Trace.info("nextPage: ", nextPage);

        if (currentPage == _EXIT_PAGE)
        {
            exitDialog.clear();
            exitDialog.addAction(Actions.removeActor());
        }

        if (currentPage != _EXIT_PAGE)
        {
            if (panels.get(currentPage) != null)
            {
                panels.get(currentPage).hide();
                panels.get(currentPage).dispose();
            }
        }

        currentPage = nextPage;

        if (currentPage != _EXIT_PAGE)
        {
            if (panels.get(currentPage) != null)
            {
                panels.get(currentPage).initialise();
                panels.get(currentPage).show();
            }
        }
    }

    @Override
    public void render(float delta)
    {
        super.update();

        if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
        {
            update();

            super.render(delta);
        }
    }

    public void draw(final SpriteBatch spriteBatch, OrthoGameCamera camera)
    {
        if (App.getAppState().peek() == StateID._STATE_MAIN_MENU)
        {
            switch (currentPage)
            {
                case _MENU_PAGE:
                case _EXIT_PAGE:
                {
                    spriteBatch.draw(foreground, 0, 0);
                }
                break;

                case _OPTIONS_PAGE:
                case _CREDITS_PAGE:
                {
                    panels.get(currentPage).draw(spriteBatch);

                    if (GdxSystem.inst().backButton != null)
                    {
                        if (GdxSystem.inst().backButton.isVisible())
                        {
                            GdxSystem.inst().backButton.setPosition(20, 20);
                        }
                    }
                }
                break;

                default:
                {
                    Trace.err("Unknown panel: " + currentPage);
                }
                break;
            }
        }
    }

    @Override
    public void show()
    {
        Trace.fileInfo();

        if (GdxSystem.inst().currentScreenID == ScreenID._GAME_SCREEN)
        {
            //
            // If moving to the TitleScene from MainScene, then
            // all objects that are unnecessary at this point
            // must be destroyed.
            App.getGameProgress().closeLastGame();
            App.getMainScene().dispose();
        }

        GdxSystem.inst().currentScreenID = ScreenID._MAIN_MENU;
        App.getAppState().set(StateID._STATE_MAIN_MENU);

        super.show();

        App.getBaseRenderer().resetCameraZoom();
        App.getBaseRenderer().disableAllCameras();

        App.getBaseRenderer().backgroundCamera.isInUse = true;
        App.getBaseRenderer().hudGameCamera.isInUse    = true;
        App.getBaseRenderer().isDrawingStage           = true;

        initialise();
    }

    @Override
    public void hide()
    {
        Trace.fileInfo();

        super.hide();

        dispose();
    }

    @Override
    public void loadImages()
    {
        App.getBaseRenderer().backgroundImage = App.getAssets().loadSingleAsset(GameAssets._GAME_BACKGROUND_ASSET, Texture.class);

        foreground = App.getAssets().loadSingleAsset("data/title_foreground.png", Texture.class);
    }

    public MenuPage getMenuPage()
    {
        return menuPage;
    }

    @Override
    public void dispose()
    {
        Trace.fileInfo();

        menuPage.hide();
        menuPage.dispose();

        if (panels != null)
        {
            panels.clear();
            panels = null;
        }

        optionsPage = null;
        menuPage    = null;

        foreground = null;
        exitDialog = null;
    }
}
