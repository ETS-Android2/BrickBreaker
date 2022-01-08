package com.richikin.brickbreaker.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.GdxSprite;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.gdxutils.entities.IEntityManagerComponent;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec3F;

public class MapCreator implements IMapParser, Disposable
{
    public static class MapInfo
    {
        public String       mapName;
        public boolean      moveableBricks;
        public ActionStates moveType;

        public MapInfo(String name, boolean moveable, ActionStates type)
        {
            this.mapName        = name;
            this.moveableBricks = moveable;
            this.moveType       = type;
        }
    }

    //@formatter:off
    public final MapInfo[] levelList =
        {
            // -------------------------------------------------------------------------------
            new MapInfo("",                     false, ActionStates._STANDING ),
            // -------------------------------------------------------------------------------
            new MapInfo("data/maps/grid1.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid2.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid3.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid4.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid5.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid6.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid7.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid8.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid9.tmx",  false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid10.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid11.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid12.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid13.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid14.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid15.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid16.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid17.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid18.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid19.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid20.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid21.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid22.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid23.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid24.tmx", false, ActionStates._STANDING ),
            new MapInfo("data/maps/grid25.tmx", false, ActionStates._STANDING ),
            // -------------------------------------------------------------------------------
        };
    //@formatter:on

    public TmxMapLoader               tmxMapLoader;
    public TiledMapTileLayer          brickTilesLayer;
    public TiledMapTileLayer          gameTilesLayer;
    public MapObjects                 mapObjects;
    public TiledMap                   currentMap;
    public String                     currentMapName;
    public int                        currentMapIndex;
    public Array<SpriteDescriptor>    placementTiles;
    public OrthogonalTiledMapRenderer mapRenderer;
    public int                        mapLevel;

    // -----------------------------------------------------
    // Code
    // -----------------------------------------------------

    public MapCreator()
    {
        tmxMapLoader   = new TmxMapLoader();
        placementTiles = new Array<>();
        mapLevel       = 0;
    }

    @Override
    public void initialiseLevelMap()
    {
        currentMapIndex = Math.max(1, App.getLevel() % levelList.length);
        currentMapName  = levelList[currentMapIndex].mapName;
        currentMap      = tmxMapLoader.load(currentMapName);

        Trace.dbg("GameLevel      : ", App.getLevel());
        Trace.dbg("MapLevel       : ", mapLevel);
        Trace.dbg("CurrentMapIndex: ", currentMapIndex);

        //
        // Create a new MapRenderer if none exists,
        // otherwise set up the current map.
        if (mapRenderer == null)
        {
            mapRenderer = new OrthogonalTiledMapRenderer(currentMap, App.getSpriteBatch());
        }

        setGameLevelMap();
        mapRenderer.setMap(currentMap);
    }

    /**
     * Creates the positioning data for all sprites, AABB boxes and other game
     * entities, by reading this information from TiledMap .tmx files.
     */
    @Override
    public void createPositioningData()
    {
        Trace.fileInfo();

        for (IEntityManagerComponent component : App.getEntityData().getManagerList())
        {
            component.setPlaceable(false);
        }

        placementTiles.clear();

        //
        // Marker tiles for bricks.
        parseMarkerTiles();

        if (mapObjects != null)
        {
            // Collision boxes for any solid boxes
            // that may be present in the current map.
            parseMapObjects();
        }
    }

    public void render(OrthographicCamera camera)
    {
        if (gameTilesLayer != null)
        {
            mapRenderer.setView(camera);
            mapRenderer.renderTileLayer(gameTilesLayer);
        }
    }

    @Override
    public void parseMarkerTiles()
    {
        Trace.fileInfo();

        // Enable these by default
        setEntityPlaceable(GraphicID._PLAYER_MANAGER, true);
        setEntityPlaceable(GraphicID._BALL_MANAGER, true);
        setEntityPlaceable(GraphicID._PICKUP_MANAGER, true);

        for (int y = 0; y < brickTilesLayer.getHeight(); y++)
        {
            for (int x = 0; x < brickTilesLayer.getWidth(); x++)
            {
                // getCell() returns null if the raw data at x,y is zero. This is Ok
                // because, here, that means there is no marker tile to process.
                TiledMapTileLayer.Cell cell = brickTilesLayer.getCell(x, y);

                if (cell != null)
                {
                    TileID tileID = TileID.fromValue(cell.getTile().getId());

                    boolean isIgnoreTile = false;

                    switch (tileID)
                    {
                        case _BLUE_BRICK_TILE:
                        case _GREEN_BRICK_TILE:
                        case _RED_BRICK_TILE:
                        case _SILVER_BRICK_TILE:
                        case _YELLOW_BRICK_TILE:
                        case _PINK_BRICK_TILE:
                        case _ORANGE_BRICK_TILE:
                        case _PURPLE_BRICK_TILE:
                        case _DARK_PURPLE_BRICK_TILE:
                        {
                            setEntityPlaceable(GraphicID._BRICKS_MANAGER, true);
                        }
                        break;

                        //
                        // Marker tiles that should not create entries
                        // in the placements array.
                        case _PADDLE_TILE:
                        case _BALL_TILE:
                        {
                            isIgnoreTile = true;
                        }
                        break;

                        default:
                        {
                            Trace.dbg
                                (
                                    " - Unknown tile: ",
                                    tileID,
                                    ": (", tileID.get(), ")",
                                    " at ", x, ", ", y
                                );

                            isIgnoreTile = true;
                        }
                        break;
                    }

                    if (!isIgnoreTile)
                    {
                        SpriteDescriptor descriptor = App.getEntities().getDescriptor(tileID);
                        descriptor._POSITION.x = x * brickTilesLayer.getTileWidth();
                        descriptor._POSITION.y = y * brickTilesLayer.getTileHeight();
                        descriptor._INDEX      = placementTiles.size;

                        placementTiles.add(descriptor);
                    }
                }
            }
        }

        Trace.divider();
        Trace.dbg("Number of Entities created from tiles: " + placementTiles.size);
        Trace.divider();
    }

    @Override
    public void parseMapObjects()
    {
        Trace.fileInfo();

        for (MapObject mapObject : mapObjects)
        {
            if (mapObject instanceof RectangleMapObject)
            {
                if (null != mapObject.getName())
                {
                    GdxSprite entity = new GdxSprite();

                    switch (mapObject.getName())
                    {
                        case "Ceiling":
                        {
                            entity.gid          = GraphicID._CEILING;
                            entity.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;
                        }
                        break;

                        case "Left Wall":
                        case "Right Wall":
                        case "Wall":
                        {
                            entity.gid          = GraphicID._WALL;
                            entity.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;
                        }
                        break;

                        case "Floor":
                        {
                            entity.gid          = GraphicID._GROUND;
                            entity.collidesWith = Gfx.CAT_PLAYER_WEAPON;
                        }
                        break;

                        default:
                            break;
                    }

                    if (entity.gid != GraphicID.G_NO_ID)
                    {
                        entity.type         = GraphicID._OBSTACLE;
                        entity.bodyCategory = Gfx.CAT_WALL;

                        SimpleVec3F position = new SimpleVec3F
                            (
                                ((float) mapObject.getProperties().get("x")),
                                ((float) mapObject.getProperties().get("y")),
                                0
                            );

                        entity.frameWidth  = (int) ((float) mapObject.getProperties().get("width"));
                        entity.frameHeight = (int) ((float) mapObject.getProperties().get("height"));

                        entity.setCollisionObject(position.x, position.y);

                        if (App.getAppConfig().isUsingBox2DPhysics())
                        {
                            entity.b2dBody = App.getWorldModel().bodyBuilder.createStaticBody(entity);
                        }

                        App.getEntityData().addEntity(entity);

                        if (App.getAppConfig().isDevMode())
                        {
                            Trace.dbg("Added ", entity.gid);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void setGameLevelMap()
    {
        brickTilesLayer = (TiledMapTileLayer) currentMap.getLayers().get("grids layer");
        gameTilesLayer  = (TiledMapTileLayer) currentMap.getLayers().get("obstacles layer");

        MapLayer mapLayer = currentMap.getLayers().get("collision");
        if (mapLayer != null)
        {
            mapObjects = mapLayer.getObjects();
        }

        App.getMapData().tileWidth  = currentMap.getProperties().get("tilewidth", Integer.class);
        App.getMapData().tileHeight = currentMap.getProperties().get("tileheight", Integer.class);
        App.getMapData().mapWidth  = (currentMap.getProperties().get("width", Integer.class) * App.getMapData().tileWidth);
        App.getMapData().mapHeight = (currentMap.getProperties().get("height", Integer.class) * App.getMapData().tileHeight);
    }

    public ActionStates getBricksMoveType()
    {
        return levelList[currentMapIndex].moveType;
    }

    @SuppressWarnings("SameParameterValue")
    private void setEntityPlaceable(GraphicID gid, boolean placeable)
    {
        for (IEntityManagerComponent component : App.getEntityData().getManagerList())
        {
            if (component.getGID() == gid)
            {
                component.setPlaceable(placeable);
            }
        }
    }

    @Override
    public void dispose()
    {
        tmxMapLoader    = null;
        brickTilesLayer = null;
        gameTilesLayer  = null;
        mapObjects      = null;
        currentMap      = null;
        currentMapName  = null;

        mapRenderer.dispose();
        mapRenderer = null;

        placementTiles.clear();
        placementTiles = null;
    }
}
