package com.richikin.brickbreaker.maps;

import com.badlogic.gdx.utils.Array;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.brickbreaker.entities.Entities;

public class TiledUtils
{
    public Array<SpriteDescriptor> findMultiTiles(final GraphicID targetGID)
    {
        Array<SpriteDescriptor> tiles = new Array<>();

        for (SpriteDescriptor marker : App.getMapCreator().placementTiles)
        {
            if (marker._GID == targetGID)
            {
                tiles.add(marker);
            }
        }

        return tiles;
    }

    /**
     * Scans through {@link Entities#entityList} to find the
     * {@link GraphicID} which matches the supplied {@link TileID}.
     */
    public GraphicID tileToGID(TileID tileID)
    {
        GraphicID gid = GraphicID.G_NO_ID;
        int index = 0;

        while((index < App.getEntities().entityList.length) && (gid == GraphicID.G_NO_ID))
        {
            if (App.getEntities().entityList[index]._TILE == tileID)
            {
                gid = App.getEntities().entityList[index]._GID;
            }

            index++;
        }

        return gid;
    }
}
