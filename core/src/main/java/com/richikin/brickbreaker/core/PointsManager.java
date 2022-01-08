package com.richikin.brickbreaker.core;

import com.richikin.enumslib.GraphicID;

public class PointsManager
{
    static class Points
    {
        final GraphicID gid;
        final int       points;

        Points(GraphicID _gid, int _points)
        {
            this.gid    = _gid;
            this.points = _points;
        }
    }

    //@formatter:off
    private final Points[] pointsTable =
        {
            new Points(GraphicID.G_BLUE_BRICK,          10),
            new Points(GraphicID.G_GREEN_BRICK,         10),
            new Points(GraphicID.G_RED_BRICK,           10),
            new Points(GraphicID.G_SILVER_BRICK,        10),
            new Points(GraphicID.G_YELLOW_BRICK,        10),
            new Points(GraphicID.G_PINK_BRICK,          10),
            new Points(GraphicID.G_ORANGE_BRICK,        10),
            new Points(GraphicID.G_PURPLE_BRICK,        10),
            new Points(GraphicID.G_DARK_PURPLE_BRICK,   10),

            new Points(GraphicID.G_SHRINK_BONUS,        0),
            new Points(GraphicID.G_SLOWDOWN_BONUS,      0),
            new Points(GraphicID.G_SPEEDUP_BONUS,       0),
            new Points(GraphicID.G_EXPAND_BONUS,        0),
            new Points(GraphicID.G_EXTRA_LIFE,          0),

            new Points(GraphicID.G_PLUS10,              50),
            new Points(GraphicID.G_PLUS25,              100),
            new Points(GraphicID.G_PLUS50,              250),
            new Points(GraphicID.G_PLUS75,              500),
        };
    //@formatter:on

    public PointsManager()
    {
    }

    public int getPoints(GraphicID gid)
    {
        int score = 0;

        for (final Points points : pointsTable)
        {
            if (gid.equals(points.gid))
            {
                score = points.points;

                break;
            }
        }

        return score;
    }
}
