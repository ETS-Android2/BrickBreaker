package com.richikin.gdxutils.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.config.StandardSettings;
import com.richikin.gdxutils.exceptions.NotImplementedException;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.graphics.text.FontUtils;
import com.richikin.gdxutils.logging.Stats;
import com.richikin.gdxutils.logging.Trace;

import java.util.Locale;

public class DeveloperPanel extends DefaultPanel
{
    private int disableEnemiesColumn;
    private int disableEnemiesRow;
    private int glProfilerRow;
    private int glProfilerColumn;

    private static class DMEntry
    {
        final String  string;
        final String  prefName;
        final boolean isEnemy;

        DMEntry(String _string, String pref, boolean _isEnemy)
        {
            this.string   = _string;
            this.prefName = pref;
            this.isEnemy  = _isEnemy;
        }
    }

    private static final int _TABLE_COLUMNS = 3;

    private DMEntry[][]  devMenu;
    private Texture      foreground;
    private CheckBox[][] buttons;
    private TextField    heading;
    private GLProfiler   glProfiler;
    private TextButton   exitButton;
    private TextButton   buttonResetPrefs;
    private TextButton   buttonResetHiScores;
    private TextButton   buttonResetStats;
    private TextButton   buttonGLProfiler;
    private TextButton   buttonCollisionDump;

    private final int originX;
    private final int originY;

    private boolean previousDisableEnemies;
    private boolean previousExternalController;
    private boolean okToResetPrefs;

    private final boolean[] columnEnabled =
        {
            true,
            false,
            false,
        };

    // ------------------------------------------------------------
    // Code
    // ------------------------------------------------------------

    public DeveloperPanel(int x, int y)
    {
        super();

        nameID  = "Developer Panel";
        originX = x;
        originY = y;

        if (GdxApp.getAppConfig().isDevMode())
        {
            glProfiler = new GLProfiler(Gdx.graphics);
        }
    }

    @Override
    public void setup()
    {
        Trace.fileInfo();

        GdxSystem.inst().gamePaused = true;

        loadDevMenu();

        validateRowsAndColumns();

        foreground = GdxApp.getAssets().loadSingleAsset(GdxApp.getAssets().getDevPanelBackground(), Texture.class);

        okToResetPrefs = false;

        String skinFilename = GdxApp.getAssets().getSkinFilename();

        if (skinFilename.equals(""))
        {
            skin = new Skin();
        }
        else
        {
            skin = new Skin(Gdx.files.internal(GdxApp.getAssets().getSkinFilename()));
        }

        table = createTable();

        createHeading(skin);
        createButtons(skin);

        populateTable(table, skin);

        // Wrap the table in a scrollpane.
        scrollPane = new ScrollPane(table, skin);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth((float) (GfxData._HUD_WIDTH - 100));
        scrollPane.setHeight((float) (GfxData._HUD_HEIGHT - 260));
        scrollPane.setPosition(originX + 50, originY + 120);
        scrollPane.setScrollbarsOnTop(true);

        GdxApp.getStage().addActor(scrollPane);
        GdxApp.getStage().addActor(heading);
        GdxApp.getStage().addActor(exitButton);
        GdxApp.getStage().addActor(buttonResetPrefs);
        GdxApp.getStage().addActor(buttonResetHiScores);
        GdxApp.getStage().addActor(buttonResetStats);
        GdxApp.getStage().addActor(buttonGLProfiler);
        GdxApp.getStage().addActor(buttonCollisionDump);

        updatePreferencesOnEntry();

        previousDisableEnemies = buttons[disableEnemiesRow][disableEnemiesColumn].isChecked();

        if (GdxApp.getAppConfig().isDevMode())
        {
            glProfilerUpdate();
        }
    }

    private Table createTable()
    {
        Trace.fileInfo();

        Table table = new Table();
        table.top().left();
        table.pad(60, 10, 10, 10);

        Texture texture    = GdxApp.getAssets().loadSingleAsset(GdxApp.getAssets().getDevPanelBackground(), Texture.class);
        Image   background = new Image(new TextureRegion(texture));
        table.setBackground(background.getDrawable());

        return table;
    }

    private void createHeading(Skin _skin)
    {
        Trace.fileInfo();

        heading = new TextField("DEVELOPER OPTIONS", _skin);
        heading.setSize(400, 96);
        heading.setDisabled(true);
        heading.setPosition
            (
                originX + (GfxData._HUD_WIDTH / 6f),
                originY + (GfxData._HUD_HEIGHT - 80),
                Align.left
            );

        FontUtils                fontUtils = new FontUtils();
        BitmapFont               font      = fontUtils.createFont(GdxApp.getAssets().getDevPanelFont(), 32, Color.WHITE);
        TextField.TextFieldStyle style     = heading.getStyle();
        style.font = font;

        heading.setStyle(style);
    }

    private void createButtons(Skin _skin)
    {
        Trace.fileInfo();

        exitButton          = new TextButton("Back", _skin);
        buttonResetPrefs    = new TextButton("Reset Settings To Default", _skin);
        buttonResetHiScores = new TextButton("Reset HiScore Table", _skin);
        buttonResetStats    = new TextButton("Reset Stats Meters", _skin);
        buttonGLProfiler    = new TextButton("GLProfiler Dump", _skin);
        buttonCollisionDump = new TextButton("CollisionObject Breakdown", _skin);

        int x = 20;

        buttonResetPrefs.setPosition(originX + x, originY + 15);

        x += buttonResetPrefs.getWidth() + 20;

        buttonResetHiScores.setPosition(originX + x, originY + 15);

        x += buttonResetHiScores.getWidth() + 20;

        buttonResetStats.setPosition(originX + x, originY + 15);

        x += buttonResetStats.getWidth() + 20;

        buttonGLProfiler.setPosition(originX + x, originY + 15);

        x += buttonGLProfiler.getWidth() + 20;

        buttonCollisionDump.setPosition(originX + x, originY + 15);

        exitButton.setPosition(originX + 20, originY + (GfxData._HUD_HEIGHT - 100));
        exitButton.setSize(40, 40);

        createButtonListeners();
    }

    private void populateTable(Table _table, Skin _skin)
    {
        Trace.fileInfo();

        Label[] label = new Label[_TABLE_COLUMNS];

        buttons = new CheckBox[devMenu.length][_TABLE_COLUMNS];

        FontUtils fontUtils = new FontUtils();

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; columnEnabled[column] && column < _TABLE_COLUMNS; column++)
            {
                label[column] = new Label
                    (
                        (String.format(Locale.getDefault(), "%-30s", devMenu[row][column].string)).toUpperCase(),
                        _skin
                    );

                Label.LabelStyle labelStyle = label[column].getStyle();
                labelStyle.font = fontUtils.createFont(GdxApp.getAssets().getDevPanelFont(), 18, Color.WHITE);

                label[column].setStyle(labelStyle);
                label[column].setAlignment(Align.left);

                buttons[row][column] = new CheckBox("", _skin);
                buttons[row][column].setHeight(label[column].getHeight());

                CheckBox.CheckBoxStyle style = buttons[row][column].getStyle();
                style.checkboxOn  = new TextureRegionDrawable(GdxApp.getAssets().getButtonRegion("toggle_on"));
                style.checkboxOff = new TextureRegionDrawable(GdxApp.getAssets().getButtonRegion("toggle_off"));

                buttons[row][column].setStyle(style);

                if ("".equals(devMenu[row][column].string))
                {
                    buttons[row][column].setChecked(false);
                }
                else
                {
                    buttons[row][column].setChecked(GdxApp.getSettings().isEnabled(devMenu[row][column].prefName));
                }
            }

            createCheckBoxListener(row);

            for (int column = 0; columnEnabled[column] && column < _TABLE_COLUMNS; column++)
            {
                Label num = new Label("" + row + ": ", _skin);
                _table.add(num).padLeft(20);
                _table.add(label[column]);
                _table.add(buttons[row][column]);
            }

            _table.row();
        }

        _table.setVisible(true);
    }

    private void createButtonListeners()
    {
        exitButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                clearUp();
            }
        });

        buttonResetPrefs.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resetPreferencesToDefaults();
            }
        });

        buttonResetStats.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                Stats.resetAllMeters();
            }
        });

        buttonResetHiScores.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (GdxApp.getAppConfig().isDevMode())
                {
                    GdxApp.getHighScoreUtils().resetTable();

                    Trace.dbg("HISCORE Table reset to defaults.");
                }
            }
        });

        buttonGLProfiler.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (GdxApp.getAppConfig().isDevMode())
                {
                    Trace.dbg
                        (
                            "  Drawcalls: " + glProfiler.getDrawCalls()
                                + ", Calls: " + glProfiler.getCalls()
                                + ", TextureBindings: " + glProfiler.getTextureBindings()
                                + ", ShaderSwitches:  " + glProfiler.getShaderSwitches()
                                + "vertexCount: " + glProfiler.getVertexCount().value
                        );

                    glProfiler.reset();
                }
            }
        });

        buttonCollisionDump.addListener(new ClickListener()
        {
            @Override
            public void clicked(final InputEvent event, final float x, final float y)
            {
                if (GdxApp.getAppConfig().isDevMode())
                {
                    throw new NotImplementedException("DebugALL from collisionUtils...");
//                    app.collisionUtils.debugAll();
                }
            }
        });
    }

    private void createCheckBoxListener(int index)
    {
        for (int column = 0; columnEnabled[column] && column < _TABLE_COLUMNS; column++)
        {
            buttons[index][column].addListener(new ChangeListener()
            {
                @Override
                public void changed(ChangeEvent event, Actor actor)
                {
                    if (!okToResetPrefs)
                    {
                        updatePreferences();
                    }
                }
            });
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, 0, 0, GfxData._HUD_WIDTH, GfxData._HUD_HEIGHT);
        }
    }

    private void updatePreferencesOnEntry()
    {
        Trace.fileInfo();

        if (!GdxApp.getAppConfig().isDevMode())
        {
            GdxApp.getSettings().disable(StandardSettings._MENU_HEAPS);
        }

        updatePreferences();
    }

    private void updatePreferences()
    {
        if (buttons[disableEnemiesRow][disableEnemiesColumn].isChecked() != previousDisableEnemies)
        {
            setAllEnemiesEnableStatus(buttons[disableEnemiesRow][disableEnemiesColumn].isChecked());
        }

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; columnEnabled[column] && column < _TABLE_COLUMNS; column++)
            {
                GdxApp.getSettings().getPrefs().putBoolean(devMenu[row][column].prefName, buttons[row][column].isChecked());
            }
        }

        GdxApp.getSettings().getPrefs().flush();

        glProfilerUpdate();

        previousDisableEnemies = buttons[disableEnemiesRow][disableEnemiesColumn].isChecked();
    }

    private void updatePreferencesOnExit()
    {
        Trace.fileInfo();
    }

    private void glProfilerUpdate()
    {
        if (GdxApp.getAppConfig().isDevMode())
        {
            if (buttons[glProfilerRow][glProfilerColumn].isChecked())
            {
                // Profiling should be disabled on release software, hence
                // the warning suppression. Normally, not a good idea to
                // suppress such warnings but this if..else... is only
                // executed in Developer Mode.

                //noinspection LibGDXProfilingCode
                glProfiler.enable();
            }
            else
            {
                glProfiler.disable();
            }
        }
    }

    /**
     * Either ENABLE or DISABLE all enemy entities.
     *
     * @param _disable boolean TRUE to disable
     *                 boolean FALSE to enable.
     */
    private void setAllEnemiesEnableStatus(boolean _disable)
    {
        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; columnEnabled[column] && column < _TABLE_COLUMNS; column++)
            {
                if (devMenu[row][column].isEnemy)
                {
                    buttons[row][column].setChecked(!_disable);
                }
            }
        }
    }

    private void resetPreferencesToDefaults()
    {
        okToResetPrefs = true;

        GdxApp.getSettings().resetToDefaults();

//        GdxApp.getSettings().getPrefs().putBoolean(StandardSettings._DEV_MODE, GdxApp.appConfig.isDevMode());
//        GdxApp.getSettings().getPrefs().putBoolean(StandardSettings._GOD_MODE, GdxApp.appConfig.isGodMode());
        GdxApp.getSettings().getPrefs().putBoolean(StandardSettings._SIGN_IN_STATUS, GdxApp.getPlayServices().isSignedIn());

        GdxApp.getSettings().getPrefs().flush();

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; columnEnabled[column] && column < _TABLE_COLUMNS; column++)
            {
                boolean isChecked = GdxApp.getSettings().isEnabled(devMenu[row][column].prefName);

                buttons[row][column].setChecked(isChecked);
            }
        }

        okToResetPrefs = false;
    }

    private void validateRowsAndColumns()
    {
        Trace.fileInfo();

        for (int row = 0; row < devMenu.length; row++)
        {
            int length = devMenu[row].length;

            for (int column = 0; columnEnabled[column] && column < length; column++)
            {
                String prefName = devMenu[row][column].prefName;

                switch (prefName)
                {
                    case StandardSettings._DISABLE_ENEMIES:
                    {
                        disableEnemiesColumn = column;
                        disableEnemiesRow    = row;
                    }
                    break;

                    case StandardSettings._GL_PROFILER:
                    {
                        glProfilerColumn = column;
                        glProfilerRow    = row;
                    }
                    break;

                    default:
                        break;
                }
            }
        }
    }

    private void loadDevMenu()
    {
        Trace.fileInfo();

        DMEntry[][] devMenuDefaults =
            {
               {
                    new DMEntry("Google Sign In", StandardSettings._PLAY_SERVICES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Achievements", StandardSettings._ACHIEVEMENTS, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Challenges", StandardSettings._CHALLENGES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    },
                {
                    new DMEntry("Button Outlines", StandardSettings._BUTTON_BOXES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Sprite Boxes", StandardSettings._SPRITE_BOXES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Tile Boxes", StandardSettings._TILE_BOXES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Marker Tiles", StandardSettings._SPAWNPOINTS, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Box2D Physics", StandardSettings._BOX2D_PHYSICS, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("B2D Renderer", StandardSettings._B2D_RENDERER, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Use Ashley ECS", StandardSettings._USING_ASHLEY_ECS, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
               {
                   new DMEntry("", "", false),
                   new DMEntry("", "", false),
                   new DMEntry("", "", false),
                   },
                {
                    new DMEntry("Demo Scroll", StandardSettings._SCROLL_DEMO, false),
                    new DMEntry("Autoplay", StandardSettings._AUTOPLAY, false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Shader Program", StandardSettings._SHADER_PROGRAM, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Cull Sprites", StandardSettings._CULL_SPRITES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("GLProfiler", StandardSettings._GL_PROFILER, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Menu Screen", StandardSettings._MENU_SCENE, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Disable Enemies", StandardSettings._DISABLE_ENEMIES, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("Menu Page Heaps", StandardSettings._MENU_HEAPS, false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
                {
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                    new DMEntry("", "", false),
                },
            };

        devMenu = new DMEntry[devMenuDefaults.length][_TABLE_COLUMNS];

        for (int row = 0; row < devMenuDefaults.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                devMenu[row][column] = new DMEntry
                    (
                        devMenuDefaults[row][column].string,
                        devMenuDefaults[row][column].prefName,
                        devMenuDefaults[row][column].isEnemy
                    );
            }
        }
    }

    public void debugReport()
    {
        Trace.fileInfo();

        for (DMEntry[] entry : devMenu)
        {
            for (DMEntry dmEntry : entry)
            {
                if (!dmEntry.string.isEmpty())
                {
                    Trace.dbg(dmEntry.string + ": " + GdxApp.getSettings().isEnabled(dmEntry.prefName));
                }
            }
        }
    }

    private void clearActors()
    {
        Trace.fileInfo();

        for (int row = 0; row < devMenu.length; row++)
        {
            for (int column = 0; column < _TABLE_COLUMNS; column++)
            {
                if (columnEnabled[column])
                {
                    buttons[row][column].addAction(Actions.removeActor());
                }
            }
        }

        exitButton.addAction(Actions.removeActor());
        buttonResetHiScores.addAction(Actions.removeActor());
        buttonResetPrefs.addAction(Actions.removeActor());
        buttonGLProfiler.addAction(Actions.removeActor());
        buttonResetStats.addAction(Actions.removeActor());
        buttonCollisionDump.addAction(Actions.removeActor());
        heading.addAction(Actions.removeActor());

        table.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());
    }

    private void clearUp()
    {
        Trace.fileInfo();

        updatePreferencesOnExit();

        GdxApp.getAppConfig().setDeveloperPanelState(StateID._STATE_DISABLED);
        GdxSystem.inst().gamePaused = false;

        clearActors();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        foreground.dispose();
        exitButton.clear();
        buttonResetPrefs.clear();
        buttonResetHiScores.clear();
        buttonResetStats.clear();
        buttonGLProfiler.clear();
        heading.clear();
        table.clear();
        scrollPane.clear();

        foreground          = null;
        exitButton          = null;
        buttonResetPrefs    = null;
        buttonResetHiScores = null;
        buttonResetStats    = null;
        buttonGLProfiler    = null;
        heading             = null;
        table               = null;
        scrollPane          = null;
    }
}
