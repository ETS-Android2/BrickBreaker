package com.richikin.brickbreaker.entities.characters;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.richikin.brickbreaker.audio.AudioData;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.core.GameProgress;
import com.richikin.brickbreaker.core.PointsManager;
import com.richikin.brickbreaker.entities.Entities;
import com.richikin.brickbreaker.entities.GdxSprite;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.brickbreaker.ui.HeadsUpDisplay;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.physics.CollisionObject;
import com.richikin.gdxutils.physics.Movement;
import com.richikin.gdxutils.physics.aabb.ICollisionCallback;

public class Pickup extends GdxSprite implements ICollisionCallback
{
    public Pickup(GraphicID graphicID)
    {
        super(graphicID);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        bodyCategory = Gfx.CAT_PICKUP;
        collidesWith = Gfx.CAT_PLAYER;

        create(descriptor, BodyDef.BodyType.DynamicBody);

        speed.set(0.0f, 6.0f);
        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN);
        lookingAt.set(direction);

        collisionObject.clearCollision();

        addCollisionCallback(this);

        isAnimating = true;

        setActionState(ActionStates._RUNNING);
    }

    @Override
    public void update()
    {
        if (getActionState() == ActionStates._DYING)
        {
            setActionState(ActionStates._DEAD);
        }
        else
        {
            if ((sprite.getY() + frameHeight) < 0)
            {
                setActionState(ActionStates._DEAD);
            }
            else
            {
                sprite.translate
                    (
                        (speed.getX() * direction.getX()),
                        (speed.getY() * direction.getY())
                    );
            }
        }

        animate();

        updateCommon();
    }

    @Override
    public void postUpdate()
    {
        super.postUpdate();

        if (App.getGameProgress().levelCompleted || App.getGameProgress().isRestarting)
        {
            setActionState(ActionStates._DEAD);
        }
    }

    @Override
    public void tidy(int index)
    {
        collisionObject.kill();

        App.getEntityData().removeEntityAt(index);
    }

    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
        if (cobjHitting.gid == GraphicID.G_PADDLE)
        {
            boolean hit = true;

            switch (gid)
            {
                case G_SPEEDUP_BONUS:
                {
                    App.getEntities().setBallSpeed(Entities._FAST_BALL_SPEED);
                    App.getHud().bonusClockActive = true;
                    App.getHud().setBorderFrameIndex(HeadsUpDisplay._FASTBALL);
                    setActionState(ActionStates._DYING);
                }
                break;

                case G_SLOWDOWN_BONUS:
                {
                    if (App.getEntities().ballsAreFast())
                    {
                        App.getEntities().setBallSpeed(Entities._SLOW_BALL_SPEED);
                    }
                    else if (App.getEntities().ballsAreSlow())
                    {
                        App.getEntities().setBallSpeed(Entities._VERY_SLOW_BALL_SPEED);
                        App.getHud().bonusClockActive = true;
                        App.getHud().setBorderFrameIndex(HeadsUpDisplay._SLOWBALL);
                    }
                    setActionState(ActionStates._DYING);
                }
                break;

                case G_EXPAND_BONUS:
                {
                    App.getPlayer().setExpanded(true);
                    App.getHud().bonusClockActive = true;
                    App.getHud().setBorderFrameIndex(HeadsUpDisplay._EXPANDED);
                    setActionState(ActionStates._DYING);
                }
                break;

                case G_SHRINK_BONUS:
                {
                    App.getPlayer().setExpanded(false);
                    setActionState(ActionStates._DYING);
                }
                break;

                case G_BALLS_X2:
                {
                    App.getEntityData().getManagerList().get(App.getEntityManager().ballManagerIndex).addMaxCount(1);
                    setActionState(ActionStates._DYING);
                }
                break;

                case G_EXTRA_LIFE:
                {
                    App.getGameProgress().lives.add(1);
                    setActionState(ActionStates._DYING);
                }
                break;

                case G_PLUS10:
                case G_PLUS25:
                case G_PLUS50:
                case G_PLUS75:
                {
                    PointsManager points = new PointsManager();

                    App.getGameProgress().stackPush
                        (
                            GameProgress.Stack._SCORE,
                            points.getPoints(gid)
                        );
                    setActionState(ActionStates._DYING);
                }
                break;

                default:
                    hit = false;
                    break;
            }

            if (hit)
            {
                App.getAudio().startSound(AudioData.SFX_PICKUP);
            }
        }
    }

    @Override
    public void onNegativeCollision()
    {
    }
}
