package com.richikin.enumslib;

public enum GraphicID
{
    // ----------------------------
    G_PADDLE,
    G_BALL,

    // ----------------------------
    G_EXPAND_BONUS,
    G_SHRINK_BONUS,
    G_SPEEDUP_BONUS,
    G_SLOWDOWN_BONUS,
    G_BALLS_X2,
    G_PLUS10,
    G_PLUS25,
    G_PLUS50,
    G_PLUS75,
    G_EXTRA_LIFE,

    // ----------------------------
    G_BRICK,
    G_BLUE_BRICK,
    G_GREEN_BRICK,
    G_RED_BRICK,
    G_ORANGE_BRICK,
    G_YELLOW_BRICK,
    G_DARK_PURPLE_BRICK,
    G_PURPLE_BRICK,
    G_SILVER_BRICK,
    G_PINK_BRICK,

    // ----------------------------
    G_EXPLOSION12,
    G_EXPLOSION64,
    G_EXPLOSION128,
    G_EXPLOSION256,
    G_PRIZE_BALLOON,
    G_MESSAGE_BUBBLE,

    // #########################################################
    // Generic IDs
    // ----------------------------
    _MONSTER,
    _BLOCKS,
    _GROUND,
    _CEILING,
    _WALL,
    _LETHAL_OBJECT,
    _SIGN,
    _SPEECH,
    _HUD_PANEL,
    _EXIT_BOX,
    _BACKGROUND_ENTITY,
    _BRIDGE,
    _CRATER,
    _PLAYER_MANAGER,
    _BALL_MANAGER,
    _BRICKS_MANAGER,
    _PICKUP_MANAGER,

    // ----------------------------
    // Main Character type, i.e. Player
    _MAIN,

    // ----------------------------
    // Enemy Character type, but not stationary entities
    // like rocket launchers etc.
    _ENEMY,

    // ----------------------------
    // Encapsulating type, covering any collision IDs that can be stood on.
    // This will be checked against the collision object TYPE, not the NAME.
    _OBSTACLE,

    // ----------------------------
    // As above but for objects that can't be stood on and are not entities
    _DECORATION,

    // As above, but for entities
    _ENTITY,

    // ----------------------------
    // Interactive objects
    _PICKUP,
    _WEAPON,
    _INTERACTIVE,
    _PRISONER,
    _PLATFORM,

    // ----------------------------

    G_DUMMY,
    G_NO_ID;
}
