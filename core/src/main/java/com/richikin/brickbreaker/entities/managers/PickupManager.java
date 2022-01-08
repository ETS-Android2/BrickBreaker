package com.richikin.brickbreaker.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.Entities;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.entities.characters.Pickup;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec2;

public class PickupManager extends BasicEntityManager
{
    private final Array<GraphicID> pickupList;
    private       boolean          readyForNewPickup;
    private       SimpleVec2       nextPickupPosition;
    private       GraphicID        nextPickup;

    public PickupManager()
    {
        pickupList = new Array<>();

        pickupList.add(GraphicID.G_BALLS_X2);
        pickupList.add(GraphicID.G_EXPAND_BONUS);
        pickupList.add(GraphicID.G_SHRINK_BONUS);
        pickupList.add(GraphicID.G_SPEEDUP_BONUS);
        pickupList.add(GraphicID.G_SLOWDOWN_BONUS);
        pickupList.add(GraphicID.G_PLUS10);
        pickupList.add(GraphicID.G_PLUS25);
        pickupList.add(GraphicID.G_PLUS50);
        pickupList.add(GraphicID.G_PLUS75);
        pickupList.add(GraphicID.G_EXTRA_LIFE);

        pickupList.shuffle();
    }

    @Override
    public void init()
    {
        Trace.fileInfo();

        super.init();

        readyForNewPickup = false;
        nextPickup        = GraphicID.G_NO_ID;
    }

    @Override
    public void update()
    {
        if (readyForNewPickup && (activeCount == 0))
        {
            create();
        }
    }

    @Override
    public void create()
    {
        assert activeCount == 0;

        if (pickupList.contains(nextPickup, true))
        {
            SpriteDescriptor descriptor = App.getEntities().getDescriptor(nextPickup);
            descriptor._POSITION.x = nextPickupPosition.x;
            descriptor._POSITION.y = nextPickupPosition.y;
            descriptor._POSITION.z = App.getEntityUtils().getInitialZPosition(nextPickup);
            descriptor._INDEX      = App.getEntityData().getEntityMap().size;

            Pickup pickup = new Pickup(nextPickup);
            pickup.initialise(descriptor);

            App.getEntityData().addEntity(pickup);

            readyForNewPickup = false;
            activeCount++;

            logPickup(nextPickup);
        }
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._PICKUP_MANAGER;
    }

    /**
     * Choose a new pickup to be generated.
     *
     * @param x - X coordinate of where to spawn.
     * @param y - Y coordinate of where to spawn.
     */
    public void pickupPredet(int x, int y)
    {
        if (((App.getGameProgress().bricksDestroyed % 10) == 0)
            && (App.getBricksManager().getActiveCount() > 10)
            && (activeCount == 0))
        {
            nextPickup         = randomPickup();
            readyForNewPickup  = true;
            nextPickupPosition = new SimpleVec2(x, y);

            Trace.dbg("Next Pickup chosen: ", nextPickup, "   activeCount: ", activeCount);
        }
    }

    private GraphicID randomPickup()
    {
        GraphicID graphicID;
        boolean   isAllowed;

        int loopCount = 0;

        do
        {
            graphicID = pickupList.random();
            isAllowed = false;

            switch (graphicID)
            {
                //
                // Activates an extra ball, leaving two balls for
                // the paddle to deal with.
                // Does not activate the bonus timer.
                case G_BALLS_X2:
                {
                    isAllowed = (App.getEntities().balls.size < Entities._MAX_BALLS);
                }
                break;

                //
                // Triggers the paddle to expand to its bigger size.
                // Activates the bonus timer, and the paddle reverts
                // back to standard size when the timer expires.
                case G_EXPAND_BONUS:
                {
                    if (!App.getPlayer().isStretched)
                    {
                        isAllowed = true;
                    }
                }
                break;

                //
                // Resets the paddle back to standard size.
                // Only offered if the paddle is already stretched.
                case G_SHRINK_BONUS:
                {
                    isAllowed = App.getPlayer().isStretched;
                }
                break;

                //
                // Triggers the ball(s) to move at fast speed..
                // Activates the bonus timer, and the ball(s) revert(s)
                // back to normal speed when the timer expires.
                case G_SPEEDUP_BONUS:
                {
                    isAllowed = App.getEntities().ballsAreSlow() && !App.getHud().bonusClockActive;
                }
                break;

                //
                // Triggers the ball(s) to move at the normal speed
                // if the balls are currently moving fast, or slow speed
                // if the balls are currently at normal speed.
                case G_SLOWDOWN_BONUS:
                {
                    isAllowed = App.getEntities().ballsAreFast() || App.getEntities().ballsAreSlow();
                }
                break;

                //
                // Awards an extra life.
                // Obviously this is only offered if lives are less than _MAX_LIVES.
                case G_EXTRA_LIFE:
                {
                    isAllowed = App.getGameProgress().lives.hasRoom();
                }
                break;

                //
                // Adds an amount to the current score.
                case G_PLUS10:
                case G_PLUS25:
                case G_PLUS50:
                case G_PLUS75:
                {
                    isAllowed = true;
                }
                break;

                default:
                    break;
            }
        }
        while(!isAllowed && (loopCount++ < 500));

        if (!isAllowed || (loopCount >= 500))
        {
            graphicID = GraphicID.G_PLUS10;
        }

        return graphicID;
    }

    private void logPickup(GraphicID pickup)
    {
//        int meter = SystemMeters._DUMMY_METER.get();
//
//        switch (pickup)
//        {
//            case G_BALLS_X2:
//                meter = SystemMeters._BALLS_X2_BONUS.get();
//                break;
//
//            case G_EXPAND_BONUS:
//                meter = SystemMeters._EXPAND_BONUS.get();
//                break;
//
//            case G_SHRINK_BONUS:
//                meter = SystemMeters._SHRINK_BONUS.get();
//                break;
//
//            case G_SLOWDOWN_BONUS:
//                meter = SystemMeters._SLOW_DOWN_BONUS.get();
//                break;
//
//            case G_SPEEDUP_BONUS:
//                meter = SystemMeters._SPEED_UP_BONUS.get();
//                break;
//
//            default:
//                break;
//        }
//
//        if (meter != SystemMeters._DUMMY_METER.get())
//        {
//            Stats.incMeter(meter);
//        }
    }
}
