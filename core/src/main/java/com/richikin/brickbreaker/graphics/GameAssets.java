package com.richikin.brickbreaker.graphics;

public class GameAssets
{
    //
    // Asset names for all game graphics
    public static final String _BALL_ASSET              = "ball";
    public static final String _PADDLE_ASSET            = "paddle";
    public static final String _PADDLE_EXPANDED_ASSET   = "paddle_large";
    public static final String _PADDLE_GROWING_ASSET    = "paddle_growing";
    public static final String _PADDLE_SHRINKING_ASSET  = "paddle_shrinking";
    public static final String _BLUE_BRICK_ASSET        = "brick_blue";
    public static final String _GREEN_BRICK_ASSET       = "brick_green";
    public static final String _RED_BRICK_ASSET         = "brick_red";
    public static final String _SILVER_BRICK_ASSET      = "brick_silver";
    public static final String _YELLOW_BRICK_ASSET      = "brick_yellow";
    public static final String _DARK_PURPLE_BRICK_ASSET = "brick_darkpurple";
    public static final String _PURPLE_BRICK_ASSET      = "brick_purple";
    public static final String _PINK_BRICK_ASSET        = "brick_pink";
    public static final String _ORANGE_BRICK_ASSET      = "brick_orange";

    public static final String _EXPAND_ASSET     = "expand";
    public static final String _SHRINK_ASSET     = "shrink";
    public static final String _SLOWDOWN_ASSET   = "slowdown";
    public static final String _SPEEDUP_ASSET    = "speedup";
    public static final String _BALLSX2_ASSET    = "extra_ball";
    public static final String _PLUS10_ASSET     = "plus10";
    public static final String _PLUS25_ASSET     = "plus25";
    public static final String _PLUS50_ASSET     = "plus50";
    public static final String _PLUS75_ASSET     = "plus75";
    public static final String _EXTRA_LIFE_ASSET = "extra_life";

    //
    // Frame counts for animations
    public static final int _BALL_FRAMES             = 1;
    public static final int _PADDLE_FRAMES           = 3;
    public static final int _PADDLE_GROWING_FRAMES   = 8;
    public static final int _PADDLE_SHRINKING_FRAMES = 8;
    public static final int _BRICK_FRAMES            = 1;
    public static final int _EXPAND_FRAMES           = 1;
    public static final int _SHRINK_FRAMES           = 1;
    public static final int _SLOWDOWN_FRAMES         = 3;
    public static final int _SPEEDUP_FRAMES          = 3;
    public static final int _BALLSX2_FRAMES          = 1;
    public static final int _PLUS10_FRAMES           = 1;
    public static final int _PLUS25_FRAMES           = 1;
    public static final int _PLUS50_FRAMES           = 1;
    public static final int _PLUS75_FRAMES           = 1;
    public static final int _EXTRA_LIFE_FRAMES       = 1;

    //
    // Fonts and HUD assets
    public static final String _ACME_REGULAR_FONT     = "data/fonts/Acme-Regular.ttf";
    public static final String _BENZOIC_FONT          = "data/fonts/paraaminobenzoic.ttf";
    public static final String _GALAXY_FONT           = "data/fonts/galaxy.ttf";
    public static final String _PRO_WINDOWS_FONT      = "data/fonts/ProFontWindows.ttf";
    public static final String _VIDEO_PHREAK_FONT     = "data/fonts/videophreak.ttf";
    public static final String _ORBITRON_BOLD_FONT    = "data/fonts/Orbitron Bold.ttf";
    public static final String _CENSCBK_FONT          = "data/fonts/CENSCBK.ttf";
    public static final String _HUD_PANEL_ASSET       = "data/hud_panel.png";
    public static final String _SPLASH_SCREEN_ASSET   = "data/splash_screen.png";
    public static final String _BACKGROUND_ASSET      = "data/combined_background.png";
    public static final String _GAME_BACKGROUND_ASSET = "data/combined_background.png";

    public static final String _GETREADY_MSG_ASSET    = "getready";
    public static final String _BAD_LUCK_MSG_ASSET    = "bad_luck";
    public static final String _MISSED_IT_MSG_ASSET   = "missed";
    public static final String _TRY_AGAIN_MSG_ASSET   = "try_again";
    public static final String _OH_NO_MSG_ASSET       = "oh_no";
    public static final String _GAMEOVER_MSG_ASSET    = "gameover";
    public static final String _AREA_CLEAR_MSG_ASSET  = "area_cleared";
    public static final String _CREDITS_PANEL_ASSET   = "data/credits_panel.png";
    public static final String _OPTIONS_PANEL_ASSET   = "data/options_panel.png";
    public static final String _CONTROLLER_TEST_ASSET = "data/controller_test_panel.png";
    public static final String _PAUSE_PANEL_ASSET     = "data/pause_panel.png";
    public static final String _UISKIN_ASSET          = "uiskin.json";

    public static int hudPanelWidth;      // Set when object is loaded
    public static int hudPanelHeight;     //

    public static final String[] badLuckMessages =
        {
            _BAD_LUCK_MSG_ASSET,
            _MISSED_IT_MSG_ASSET,
            _TRY_AGAIN_MSG_ASSET,
            _OH_NO_MSG_ASSET,
        };
}
