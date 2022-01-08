package com.richikin.brickbreaker.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.richikin.brickbreaker.config.Settings;
import com.richikin.brickbreaker.config.Version;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.core.ScreenID;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.Vec2;
import com.richikin.gdxutils.ui.DeveloperPanel;
import com.richikin.gdxutils.ui.Scene2DUtils;

public class OptionsPage implements IUIPage
{
    private ImageButton        buttonStats;
    private ImageButton        buttonPrivacy;
    private ImageButton        buttonAbout;
    private ImageButton        buttonDevOptions;
    private ImageButton        buttonGoogle;
    private CheckBox           musicCheckBox;
    private CheckBox           fxCheckBox;
    private Texture            foreground;
    private Skin               skin;
    private ScreenID           activePanel;
    private DeveloperPanel     developerPanel;
    private StatsPanel         statsPanel;
    private PrivacyPolicyPanel privacyPolicyPanel;
    private boolean            justFinishedOptionsPanel;
    private boolean            justFinishedStatsPanel;
    private boolean            justFinishedPrivacyPanel;
    private boolean            enteredDeveloperPanel;
    private Vec2               panelPos;
    private Label              versionLabel;

    public OptionsPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.fileInfo();

        if (GdxSystem.inst().currentScreenID == ScreenID._MAIN_MENU)
        {
            if (GdxSystem.inst().backButton != null)
            {
                GdxSystem.inst().showAndEnableBackButton();
                GdxSystem.inst().backButton.setChecked(false);
            }
        }

        foreground = GdxApp.getAssets().loadSingleAsset(GdxApp.getAssets().getOptionsPanelAsset(), Texture.class);
        skin       = new Skin(Gdx.files.internal(GdxApp.getAssets().getSkinFilename()));

        panelPos = new Vec2
            (
                (int) (App.getBaseRenderer().hudGameCamera.camera.viewportWidth - foreground.getWidth()) / 2,
                (int) (App.getBaseRenderer().hudGameCamera.camera.viewportHeight - foreground.getHeight()) / 2
            );

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        versionLabel = scene2DUtils.addLabel
            (
                Version.getDisplayVersion(),
                new Vec2(panelPos.x, panelPos.y - 30),
                Color.WHITE,
                new Skin(Gdx.files.internal(GdxApp.getAssets().getSkinFilename()))
            );
        versionLabel.setFontScale(1.35f, 1.35f);
        versionLabel.setAlignment(Align.left);

        populateTable();
        createButtonListeners();
        createCheckboxListeners();
        updateSettingsOnEntry();

        activePanel              = ScreenID._SETTINGS_SCREEN;
        developerPanel           = new DeveloperPanel(0, 0);
        enteredDeveloperPanel    = false;
        justFinishedOptionsPanel = false;
        justFinishedPrivacyPanel = false;
        justFinishedStatsPanel   = false;

        GdxApp.getAppConfig().setDeveloperPanelState(StateID._STATE_DISABLED);
    }

    @Override
    public boolean update()
    {
        if (GdxSystem.inst().backButton.isChecked())
        {
            switch (activePanel)
            {
                case _STATS_SCREEN:
                {
                    if (statsPanel != null)
                    {
                        statsPanel.dispose();
                    }

                    statsPanel               = null;
                    justFinishedOptionsPanel = false;
                }
                break;

                case _INSTRUCTIONS_SCREEN:
                {
                }
                break;

                case _PRIVACY_POLICY_SCREEN:
                {
                    if (privacyPolicyPanel != null)
                    {
                        privacyPolicyPanel.dispose();
                    }

                    privacyPolicyPanel = null;
                    justFinishedOptionsPanel = false;
                }
                break;

                case _DEVELOPER_PANEL:
                {
                    developerPanel.update();
                }
                break;

                default:
                {
                    updateSettings();
                    justFinishedOptionsPanel = true;
                }
                break;
            }

            if (!justFinishedOptionsPanel)
            {
                showActors(true);
                activePanel = ScreenID._SETTINGS_SCREEN;
                GdxSystem.inst().backButton.setChecked(false);
            }
        }

        if (enteredDeveloperPanel && (App.getAppConfig().getDeveloperPanelState() == StateID._STATE_DISABLED))
        {
            enteredDeveloperPanel = false;
            showActors(true);

            if (GdxSystem.inst().currentScreenID == ScreenID._MAIN_MENU)
            {
                GdxSystem.inst().backButton.setVisible(true);
                GdxSystem.inst().backButton.setDisabled(false);
            }
        }

        return justFinishedOptionsPanel;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        switch (activePanel)
        {
            case _STATS_SCREEN:
                statsPanel.draw(spriteBatch);
                break;

            case _PRIVACY_POLICY_SCREEN:
                privacyPolicyPanel.draw(spriteBatch);
                break;

            case _DEVELOPER_PANEL:
                developerPanel.draw(spriteBatch);
                break;

            case _SETTINGS_SCREEN:
            default:
            {
                if (foreground != null)
                {
                    spriteBatch.draw(foreground, panelPos.x, panelPos.y);
                }
            }
            break;
        }
    }

    @Override
    public void show()
    {
        Trace.fileInfo();

        showActors(true);

        GdxSystem.inst().showAndEnableBackButton();
    }

    @Override
    public void hide()
    {
        Trace.fileInfo();

        showActors(false);
    }

    private void showActors(boolean visibility)
    {
        buttonGoogle.setVisible(visibility);
        buttonPrivacy.setVisible(visibility);
        buttonDevOptions.setVisible(visibility);
        buttonAbout.setVisible(visibility);
        buttonStats.setVisible(visibility);

        musicCheckBox.setVisible(visibility);
        fxCheckBox.setVisible(visibility);

        versionLabel.setVisible(visibility);
    }

    private void updateSettings()
    {
        App.getSettings().getPrefs().putBoolean(Settings._MUSIC_ENABLED, musicCheckBox.isChecked());
        App.getSettings().getPrefs().putBoolean(Settings._SOUNDS_ENABLED, fxCheckBox.isChecked());
        App.getSettings().getPrefs().flush();
    }

    private void updateSettingsOnEntry()
    {
        musicCheckBox.setChecked(App.getSettings().isEnabled(Settings._MUSIC_ENABLED));
        fxCheckBox.setChecked(App.getSettings().isEnabled(Settings._SOUNDS_ENABLED));
    }

    private void createCheckboxListeners()
    {
        if (musicCheckBox != null)
        {
            musicCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    App.getSettings().getPrefs().putBoolean(Settings._MUSIC_ENABLED, musicCheckBox.isChecked());
                    App.getSettings().getPrefs().flush();
                }
            });
        }

        if (fxCheckBox != null)
        {
            fxCheckBox.addListener(new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    App.getSettings().getPrefs().putBoolean(Settings._SOUNDS_ENABLED, fxCheckBox.isChecked());
                    App.getSettings().getPrefs().flush();
                }
            });
        }
    }

    private void createButtonListeners()
    {
        /*
         * Privacy policy button.
         * Displays the privacy policy on screen, for
         * the players reference.
         */
        if (buttonPrivacy != null)
        {
            buttonPrivacy.addListener(new ClickListener()
            {
                public void clicked(InputEvent event, float x, float y)
                {
                    if (privacyPolicyPanel == null)
                    {
                        showActors(false);
                        justFinishedPrivacyPanel = false;

                        privacyPolicyPanel = new PrivacyPolicyPanel(0, 0);
                        privacyPolicyPanel.setup();

                        activePanel = ScreenID._PRIVACY_POLICY_SCREEN;
                    }
                }
            });
        }

        if (App.getAppConfig().isDevMode())
        {
            /*
             * Statistics button.
             * Displays the in-game statistics.
             */
            if (buttonStats != null)
            {
                buttonStats.addListener(new ClickListener()
                {
                    public void clicked(InputEvent event, float x, float y)
                    {
                        if (statsPanel == null)
                        {
                            showActors(false);
                            justFinishedStatsPanel = false;

                            statsPanel = new StatsPanel(0, 0);
                            statsPanel.setup();

                            activePanel = ScreenID._STATS_SCREEN;
                        }
                    }
                });
            }

            /*
             * Developer Options Button.
             * Provides a button for accessing DEV MODE ONLY game option settings
             */
            if (buttonDevOptions != null)
            {
                buttonDevOptions.addListener(new ClickListener()
                {
                    public void clicked(InputEvent event, float x, float y)
                    {
                        if (App.getAppConfig().getDeveloperPanelState() == StateID._STATE_DISABLED)
                        {
                            App.getAppConfig().setDeveloperPanelState(StateID._STATE_ENABLED);
                            enteredDeveloperPanel = true;

                            showActors(false);

                            GdxSystem.inst().backButton.setVisible(false);
                            GdxSystem.inst().backButton.setDisabled(true);

                            developerPanel.setup();
                        }
                    }
                });
            }
        }

        /*
         * Instructions button.
         * Displays the Instructions / Game objectives on
         * screen, for the players reference.
         */
//        if (buttonAbout != null)
//        {
//            buttonAbout.addListener(new ClickListener()
//            {
//                public void clicked(InputEvent event, float x, float y)
//                {
//                }
//            });
//        }

        /*
         * Google Login status button.
         * Shows 'Sign Out' if signed in,
         * and 'Sign In' if signed out, obviously.
         */
//        if (buttonGoogle != null)
//        {
//            buttonGoogle.addListener(new ClickListener()
//            {
//                public void clicked(InputEvent event, float x, float y)
//                {
//                }
//            });
//        }
    }

    private void populateTable()
    {
        Trace.fileInfo();

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        // ----------
        musicCheckBox = scene2DUtils.addCheckBox("toggle_on", "toggle_off", panelPos.x + 417, panelPos.y + 364, Color.WHITE, skin);
        fxCheckBox    = scene2DUtils.addCheckBox("toggle_on", "toggle_off", panelPos.x + 417, panelPos.y + 312, Color.WHITE, skin);

        // ----------
        if (GdxSystem.inst().currentScreenID == ScreenID._MAIN_MENU)
        {
            buttonPrivacy = scene2DUtils.addButton
                (
                    "new_privacy_policy_button",
                    "new_privacy_policy_button_pressed",
                    panelPos.x + 30,
                    panelPos.y + 228
                );

            buttonAbout = scene2DUtils.addButton
                (
                    "blank_button",
                    "blank_button_pressed",
                    panelPos.x + 30,
                    panelPos.y + 181
                );

            // ----------
            buttonGoogle = scene2DUtils.addButton
                (
                    "signedOutButton",
                    "signedInButton",
                    panelPos.x + 118,
                    panelPos.y + 405
                );
        }

        // ----------
        buttonDevOptions = scene2DUtils.addButton
            (
                App.getAppConfig().isDevMode() ? "new_developer_options_button" : "blank_button",
                App.getAppConfig().isDevMode() ? "new_developer_options_button_pressed" : "blank_button",
                panelPos.x + 280,
                panelPos.y + 228
            );

        buttonStats = scene2DUtils.addButton
            (
                App.getAppConfig().isDevMode() ? "new_stats_button" : "blank_button",
                App.getAppConfig().isDevMode() ? "new_stats_button_pressed" : "blank_button",
                panelPos.x + 280,
                panelPos.y + 181
            );

        showActors(true);
    }

    @Override
    public void dispose()
    {
        Trace.fileInfo();

        GdxSystem.inst().backButton.setVisible(false);
        GdxSystem.inst().backButton.setDisabled(true);

        if (buttonStats != null)
        {
            buttonStats.addAction(Actions.removeActor());
            buttonStats = null;
        }

        if (buttonPrivacy != null)
        {
            buttonPrivacy.addAction(Actions.removeActor());
            buttonPrivacy = null;
        }

        if (buttonAbout != null)
        {
            buttonAbout.addAction(Actions.removeActor());
            buttonAbout = null;
        }

        if (buttonDevOptions != null)
        {
            buttonDevOptions.addAction(Actions.removeActor());
            buttonDevOptions = null;
        }

        musicCheckBox.addAction(Actions.removeActor());
        musicCheckBox = null;
        fxCheckBox.addAction(Actions.removeActor());
        fxCheckBox = null;

        versionLabel.addAction(Actions.removeActor());
        versionLabel = null;

        if (buttonGoogle != null)
        {
            buttonGoogle.addAction(Actions.removeActor());
            buttonGoogle = null;
        }

        App.getAssets().unloadAsset(GameAssets._OPTIONS_PANEL_ASSET);
        App.getAssets().unloadAsset(GameAssets._CONTROLLER_TEST_ASSET);

        foreground = null;
        skin       = null;
    }
}
