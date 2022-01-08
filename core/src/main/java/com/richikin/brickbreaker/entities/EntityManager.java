package com.richikin.brickbreaker.entities;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.characters.Ball;
import com.richikin.brickbreaker.entities.managers.BallManager;
import com.richikin.brickbreaker.entities.managers.BricksManager;
import com.richikin.brickbreaker.entities.managers.PickupManager;
import com.richikin.brickbreaker.entities.managers.PlayerManager;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.entities.IEntityComponent;
import com.richikin.gdxutils.entities.IEntityManagerComponent;
import com.richikin.gdxutils.logging.Trace;

public class EntityManager implements Disposable
{
    public int          ballManagerIndex;
    public int          bricksManagerIndex;
    public int          pickupManagerIndex;
    public int          playerIndex;
    public boolean      playerReady;

    private PlayerManager playerManager;
    private RenderSystem  renderSystem;

    public EntityManager()
    {
    }

    public void initialise()
    {
        renderSystem  = new RenderSystem();
        playerManager = new PlayerManager();

        bricksManagerIndex = App.getEntityData().addManager((new BricksManager()));
        pickupManagerIndex = App.getEntityData().addManager(new PickupManager());
        ballManagerIndex   = App.getEntityData().addManager(new BallManager());
    }

    public void initialiseForLevel()
    {
        Trace.fileInfo();

        GdxSystem.inst().entitiesExist = false;

        if (App.getPlayer() == null)
        {
            // Main player needs creating
            playerManager.init();
        }
        else
        {
            // Set back to start positions, and reset relevant properties
            // such as movement, collision state etc.
            App.getPlayer().setup(true);
        }

        // Initialise all other entities such as bricks, bonuses, balls etc.
        // The main player character isn't included here because of the
        // conditions described above.
        for (final IEntityManagerComponent component : App.getEntityData().getManagerList())
        {
            if (component.isPlaceable())
            {
                component.init();
            }
        }

        App.getGameProgress().bricksDestroyed = 0;

        updateIndexes();
        establishLinks();

        GdxSystem.inst().entitiesExist = true;

        assert(App.getEntityUtils().findNumberOfGid(GraphicID.G_PADDLE) == 1);
    }

    /**
     * Update all sprite entity actions. Does not update if the game is paused.
     * The main player sprite is updated after all others so that it can react
     * to any other entities actions.
     * Entity Managers are also updated here, after all entity sprites.
     */
    public void updateSprites()
    {
        if (isEntityUpdateAllowed() && !GdxSystem.inst().gamePaused)
        {
            //
            // Update all non-player entities.
            for (int i = 0; i < App.getEntityData().getEntityMap().size; i++)
            {
                GdxSprite entity = (GdxSprite) App.getEntityData().getEntity(i);

                if ((entity.getActionState() != ActionStates._DEAD)
                    && (entity.gid != GraphicID.G_BALL)
                    && (entity.gid != GraphicID.G_PADDLE))
                {
                    entity.preUpdate();
                    entity.update();
                }
            }

            //
            // Ball, and secondary Ball if enabled.
            // Updated after other entities, but before Main Player
            // to allow for destroying at the end of each level.
            // A new ball will be generated for each level.
            for (int i = 0; i < App.getEntities().balls.size; i++)
            {
                if ((App.getBall(i) != null) && (App.getBall(i).getActionState() != ActionStates._DEAD))
                {
                    App.getBall(i).preUpdate();
                    App.getBall(i).update();
                }
            }

            //
            // Main Player, updated after all other entities.
            // Updated last to allow for possible reacting to
            // other entities actions.
            if (playerReady && (App.getPlayer().getActionState() != ActionStates._DEAD))
            {
                App.getPlayer().preUpdate();
                App.getPlayer().update();
            }

            //
            // Update the various entity managers. These updates will check
            // to see if any entities need re-spawning etc.
            if (!App.getGameProgress().levelCompleted)
            {
                for (final IEntityManagerComponent component : App.getEntityData().getManagerList())
                {
                    component.update();
                }
            }
        }
    }

    public void tidySprites()
    {
        if (App.getEntityData().getEntityMap() != null)
        {
            for (int i = 0; i < App.getEntityData().getEntityMap().size; i++)
            {
                GdxSprite entity = (GdxSprite) App.getEntityData().getEntity(i);

                if (entity != null)
                {
                    if (entity.getActionState() != ActionStates._DEAD)
                    {
                        entity.postUpdate();
                    }

                    //
                    // NB: entity might have died in postUpdate, which is
                    // why this next if() is not an 'else'.
                    if (entity.getActionState() == ActionStates._DEAD)
                    {
                        switch (entity.gid)
                        {
                            // -----------------
                            case G_PADDLE:
                            case G_NO_ID:
                            case _GROUND:
                            {
                            }
                            break;

                            // -----------------
                            case G_BRICK:
                            case G_GREEN_BRICK:
                            case G_BLUE_BRICK:
                            case G_DARK_PURPLE_BRICK:
                            case G_ORANGE_BRICK:
                            case G_PINK_BRICK:
                            case G_PURPLE_BRICK:
                            case G_RED_BRICK:
                            case G_SILVER_BRICK:
                            case G_YELLOW_BRICK:
                            {
                                entity.tidy(i);
                                App.getBricksManager().free();
                            }
                            break;

                            // -----------------
                            case G_BALLS_X2:
                            case G_SPEEDUP_BONUS:
                            case G_EXPAND_BONUS:
                            case G_SHRINK_BONUS:
                            case G_SLOWDOWN_BONUS:
                            case G_PLUS10:
                            case G_PLUS25:
                            case G_PLUS50:
                            case G_PLUS75:
                            case G_EXTRA_LIFE:
                            {
                                entity.tidy(i);
                                App.getPickupManager().free();
                            }
                            break;

                            case G_BALL:
                            {
                                entity.tidy(i);
                                App.getEntities().balls.removeIndex(((Ball) entity).ballNumber);
                                App.getEntityData().getManagerList().get(ballManagerIndex).free();
                            }
                            break;

                            // -----------------
                            default:
                            {
                                entity.collisionObject.kill();
                                App.getEntityData().removeEntityAt(i);
                            }
                        }

                        updateIndexes();
                    }
                }
            }

            App.getCollisionUtils().tidy();

            if (App.getBricksManager().getActiveCount() == 0)
            {
                if (App.getEntities().balls.isEmpty())
                {
                    App.getGameProgress().levelCompleted = true;
                }
            }
        }
    }

    public void drawSprites()
    {
        if (renderSystem != null)
        {
            renderSystem.drawSprites();
        }
    }

    // TODO: 09/12/2021 - Does this go far enough? Should it update all entities?
    public void updateIndexes()
    {
        playerIndex = 0;

        for (int i = 0; i < App.getEntityData().getEntityMap().size; i++)
        {
            GdxSprite entity = (GdxSprite) App.getEntityData().getEntity(i);

            if (entity != null)
            {
                entity.spriteNumber = i;

                if (entity.gid == GraphicID.G_PADDLE)
                {
                    playerIndex = i;
                }
            }
        }
    }

    /**
     * Establish links between certain entities.
     * i.e Hitting a switch opens a specific door, so those
     * two entities must be linked.
     */
    public void establishLinks()
    {
        for (IEntityComponent gdxSprite : App.getEntityData().getEntityMap())
        {
            if (((GdxSprite) gdxSprite).isLinked)
            {
                for (int j = 0; j < App.getEntityData().getEntityMap().size; j++)
                {
                    IEntityComponent sprite = App.getEntityData().getEntityMap().get(j);

                    if (sprite.getIndex() != gdxSprite.getIndex())
                    {
                        if (((GdxSprite) sprite).link == ((GdxSprite) gdxSprite).link)
                        {
                            ((GdxSprite) sprite).link    = gdxSprite.getIndex();
                            ((GdxSprite) gdxSprite).link = sprite.getIndex();
                        }
                    }
                }
            }
        }
    }

    public boolean isEntityUpdateAllowed()
    {
        return (GdxSystem.inst().entitiesExist && !GdxSystem.inst().quitToMainMenu);
    }

    private void debugEntities()
    {
        Trace.fileInfo();

        for (IEntityComponent gdxSprite : App.getEntityData().getEntityMap())
        {
            Trace.info("EntityMap[", gdxSprite.getIndex(), "]: ", gdxSprite.getGID().name());
        }
    }

    @Override
    public void dispose()
    {
        playerManager = null;
        renderSystem  = null;
    }
}
