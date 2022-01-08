package com.richikin.brickbreaker.entities.managers;

import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.characters.Brick;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.logging.Trace;

public class BricksManager extends BasicEntityManager
{
    private final GraphicID[] bricksList =
        {
            // -----------------------
            GraphicID.G_BLUE_BRICK,
            GraphicID.G_GREEN_BRICK,
            GraphicID.G_RED_BRICK,
            // -----------------------
            GraphicID.G_SILVER_BRICK,
            GraphicID.G_YELLOW_BRICK,
            GraphicID.G_PINK_BRICK,
            // -----------------------
            GraphicID.G_ORANGE_BRICK,
            GraphicID.G_PURPLE_BRICK,
            GraphicID.G_DARK_PURPLE_BRICK,
            // -----------------------
        };

    public BricksManager()
    {
    }

    @Override
    public void init()
    {
        Trace.fileInfo();

        super.init();

        create();
    }

    @Override
    public void create()
    {
        Trace.fileInfo();

        if (App.getEntityUtils().canUpdate(GraphicID.G_BRICK))
        {
            for (int i = 0; i < App.getMapCreator().placementTiles.size; i++)
            {
                switch (App.getMapCreator().placementTiles.get(i)._GID)
                {
                    case G_BLUE_BRICK:
                    case G_GREEN_BRICK:
                    case G_DARK_PURPLE_BRICK:
                    case G_ORANGE_BRICK:
                    case G_PINK_BRICK:
                    case G_PURPLE_BRICK:
                    case G_RED_BRICK:
                    case G_YELLOW_BRICK:
                    case G_SILVER_BRICK:
                    {
                        Brick brick = new Brick(App.getMapCreator().placementTiles.get(i)._GID);
                        brick.initialise(App.getMapCreator().placementTiles.get(i));
                        brick.type = GraphicID.G_BRICK;
                        App.getEntityData().addEntity(brick);

                        activeCount++;
                    }
                    break;

                    default:
                        break;
                }
            }
        }
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._BRICKS_MANAGER;
    }
}
