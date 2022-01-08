package com.richikin.brickbreaker.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.richikin.brickbreaker.entities.characters.Ball;
import com.richikin.brickbreaker.entities.characters.Paddle;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec2;

public class Entities
{
    /**
     * Table of SpriteDescriptors describing the
     * basic properties of entities.
     * Used to create placement tiles.
     */
    public final SpriteDescriptor[] entityList =
        {
            // -----------------------------------------------------
            // Main Characters
            new SpriteDescriptor
                (
                    "Paddle",
                    GraphicID.G_PADDLE, GraphicID._MAIN,
                    GameAssets._PADDLE_ASSET, GameAssets._PADDLE_FRAMES,
                    new SimpleVec2(300, 60),
                    Animation.PlayMode.LOOP,
                    TileID._PADDLE_TILE
                ),
            new SpriteDescriptor
                (
                    "Ball",
                    GraphicID.G_BALL, GraphicID._MAIN,
                    GameAssets._BALL_ASSET, GameAssets._BALL_FRAMES,
                    new SimpleVec2(64, 64),
                    Animation.PlayMode.LOOP,
                    TileID._BALL_TILE
                ),

            // -----------------------------------------------------
            // Hazards and Bonuses
            new SpriteDescriptor
                (
                    "Expand",
                    GraphicID.G_EXPAND_BONUS, GraphicID._PICKUP,
                    GameAssets._EXPAND_ASSET, GameAssets._EXPAND_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._EXPAND_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Shrink",
                    GraphicID.G_SHRINK_BONUS, GraphicID._PICKUP,
                    GameAssets._SHRINK_ASSET, GameAssets._SHRINK_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._SHRINK_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Speed Up",
                    GraphicID.G_SPEEDUP_BONUS, GraphicID._PICKUP,
                    GameAssets._SPEEDUP_ASSET, GameAssets._SPEEDUP_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._SPEEDUP_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Slow Down",
                    GraphicID.G_SLOWDOWN_BONUS, GraphicID._PICKUP,
                    GameAssets._SLOWDOWN_ASSET, GameAssets._SLOWDOWN_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._SLOWDOWN_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Plus 10",
                    GraphicID.G_PLUS10, GraphicID._PICKUP,
                    GameAssets._PLUS10_ASSET, GameAssets._PLUS10_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._PLUS10_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Plus 25",
                    GraphicID.G_PLUS25, GraphicID._PICKUP,
                    GameAssets._PLUS25_ASSET, GameAssets._PLUS25_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._PLUS25_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Plus 50",
                    GraphicID.G_PLUS50, GraphicID._PICKUP,
                    GameAssets._PLUS50_ASSET, GameAssets._PLUS50_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._PLUS50_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Plus 75",
                    GraphicID.G_PLUS75, GraphicID._PICKUP,
                    GameAssets._PLUS75_ASSET, GameAssets._PLUS75_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._PLUS75_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Balls x2",
                    GraphicID.G_BALLS_X2, GraphicID._PICKUP,
                    GameAssets._BALLSX2_ASSET, GameAssets._BALLSX2_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._SLOWDOWN_BONUS_TILE
                ),
            new SpriteDescriptor
                (
                    "Extra Life",
                    GraphicID.G_EXTRA_LIFE, GraphicID._PICKUP,
                    GameAssets._EXTRA_LIFE_ASSET, GameAssets._EXTRA_LIFE_FRAMES,
                    new SimpleVec2(128, 128),
                    Animation.PlayMode.LOOP,
                    TileID._EXTRA_LIFE_TILE
                ),

            // -----------------------------------------------------
            // Bricks
            new SpriteDescriptor
                (
                    "Blue Brick",
                    GraphicID.G_BLUE_BRICK, GraphicID._ENTITY,
                    GameAssets._BLUE_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._BLUE_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Red Brick",
                    GraphicID.G_RED_BRICK, GraphicID._ENTITY,
                    GameAssets._RED_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._RED_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Green Brick",
                    GraphicID.G_GREEN_BRICK, GraphicID._ENTITY,
                    GameAssets._GREEN_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._GREEN_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Yellow Brick",
                    GraphicID.G_YELLOW_BRICK, GraphicID._ENTITY,
                    GameAssets._YELLOW_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._YELLOW_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Silver Brick",
                    GraphicID.G_SILVER_BRICK, GraphicID._ENTITY,
                    GameAssets._SILVER_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._SILVER_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Pink Brick",
                    GraphicID.G_PINK_BRICK, GraphicID._ENTITY,
                    GameAssets._PINK_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._PINK_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Orange Brick",
                    GraphicID.G_ORANGE_BRICK, GraphicID._ENTITY,
                    GameAssets._ORANGE_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._ORANGE_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Purple Brick",
                    GraphicID.G_PURPLE_BRICK, GraphicID._ENTITY,
                    GameAssets._PURPLE_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._PURPLE_BRICK_TILE
                ),
            new SpriteDescriptor
                (
                    "Dark Purple Brick",
                    GraphicID.G_DARK_PURPLE_BRICK, GraphicID._ENTITY,
                    GameAssets._DARK_PURPLE_BRICK_ASSET, GameAssets._BRICK_FRAMES,
                    new SimpleVec2(202, 62),
                    Animation.PlayMode.LOOP,
                    TileID._DARK_PURPLE_BRICK_TILE
                ),

            // -----------------------------------------------------
            // Obstacles
            new SpriteDescriptor
                (
                    "Wall",
                    GraphicID._WALL, GraphicID._OBSTACLE,
                    null, 0,
                    new SimpleVec2(202, 124),
                    Animation.PlayMode.NORMAL,
                    TileID._WALL_TILE
                ),
            };

    public static final int _MAX_BALLS            = 2;
    public static final int _VERY_SLOW_BALL_SPEED = 8;
    public static final int _SLOW_BALL_SPEED      = 16;
    public static final int _FAST_BALL_SPEED      = 24;

    public Paddle      paddle;
    public Array<Ball> balls;

    private float ballSpeed;

    // --------------------------------------------------------
    // CODE
    // --------------------------------------------------------

    public Entities()
    {
    }

    public void setup()
    {
        balls     = new Array<>();
        ballSpeed = _SLOW_BALL_SPEED;
    }

    public boolean ballsAreSlow()
    {
        return ballSpeed == _SLOW_BALL_SPEED;
    }

    public boolean ballsAreVerySlow()
    {
        return ballSpeed == _VERY_SLOW_BALL_SPEED;
    }

    public boolean ballsAreFast()
    {
        return ballSpeed == _FAST_BALL_SPEED;
    }

    public void setBallSpeed(int speed)
    {
        ballSpeed = speed;
    }

    public float getBallSpeed()
    {
        return Math.max(ballSpeed, _VERY_SLOW_BALL_SPEED);
    }

    public SimpleVec2 getAssetSize(GraphicID _gid)
    {
        SimpleVec2 size = new SimpleVec2();

        for (final SpriteDescriptor descriptor : entityList)
        {
            if (descriptor._GID == _gid)
            {
                size = descriptor._SIZE;
            }
        }

        if (size.isEmpty())
        {
            Trace.fileInfo();
            Trace.dbg("***** Size for " + _gid + " not found! *****");
        }

        return size;
    }

    public int getDescriptorIndex(GraphicID gid)
    {
        int     index      = 0;
        int     defsIndex  = 0;
        boolean foundIndex = false;

        for (SpriteDescriptor descriptor : entityList)
        {
            if (descriptor._GID == gid)
            {
                defsIndex  = index;
                foundIndex = true;
            }

            index++;
        }

        if (!foundIndex)
        {
            Trace.dbg("INDEX FOR " + gid + " NOT FOUND!!!");
        }

        return defsIndex;
    }

    public int getDescriptorIndex(TileID tileID)
    {
        int     index      = 0;
        int     defsIndex  = 0;
        boolean foundIndex = false;

        for (SpriteDescriptor descriptor : entityList)
        {
            if (descriptor._TILE == tileID)
            {
                defsIndex  = index;
                foundIndex = true;
            }

            index++;
        }

        if (!foundIndex)
        {
            Trace.dbg("INDEX FOR " + tileID + " NOT FOUND!!!");
        }

        return defsIndex;
    }

    public SpriteDescriptor getDescriptor(GraphicID gid)
    {
        SpriteDescriptor descriptor = new SpriteDescriptor();

        descriptor.set(entityList[getDescriptorIndex(gid)]);

        return descriptor;
    }

    public SpriteDescriptor getDescriptor(TileID tileID)
    {
        SpriteDescriptor descriptor = new SpriteDescriptor();

        descriptor.set(entityList[getDescriptorIndex(tileID)]);

        return descriptor;
    }

    public void dispose()
    {
        balls.clear();
        balls  = null;
        paddle = null;
    }
}
