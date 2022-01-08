package com.richikin.brickbreaker.entities.characters;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.richikin.brickbreaker.audio.AudioData;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.Entities;
import com.richikin.brickbreaker.entities.GdxSprite;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.SimpleVec3;
import com.richikin.gdxutils.physics.CollisionObject;
import com.richikin.gdxutils.physics.Movement;
import com.richikin.gdxutils.physics.aabb.ICollisionCallback;

public class Ball extends GdxSprite implements ICollisionCallback
{
    public int ballNumber;

    public Ball()
    {
        super(GraphicID.G_BALL);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        Trace.fileInfo();

        bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_FIXED_ENEMY | Gfx.CAT_WALL;

        create(descriptor, BodyDef.BodyType.DynamicBody);

        addCollisionCallback(this);

        setup(true);
    }

    public void setup(boolean isSpawning)
    {
        Trace.fileInfo();

        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL);
        lookingAt.set(direction);

        isAnimating = true;

        sprite.setRotation(0);
        sprite.setPosition(initXYZ.getX(), initXYZ.getY());

        App.getEntities().setBallSpeed(Entities._SLOW_BALL_SPEED);

        collisionObject.clearCollision();

        setActionState(isSpawning ? ActionStates._SPAWNING : ActionStates._STANDING);
    }

    @Override
    public SimpleVec3 getPositionModifier()
    {
        return new SimpleVec3
            (
                ((App.getPlayer().frameWidth / 2) - (this.frameWidth / 2)),
                App.getPlayer().frameHeight,
                0
            );
    }

    @Override
    public void preUpdate()
    {
        if (getActionState() == ActionStates._RESTARTING)
        {
            sprite.setPosition(initXYZ.getX(), initXYZ.getY());
            setActionState(ActionStates._SPAWNING);
        }

        super.preUpdate();
    }

    @Override
    public void update()
    {
        if (App.getAppState().peek() == StateID._STATE_PAUSED)
        {
            setActionState(ActionStates._PAUSED);
        }

        updateBall();

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

        App.getBallManager().setMaxCount(1);
    }

    private void updateBall()
    {
        switch (getActionState())
        {
            case _SPAWNING:
            case _STANDING:
            {
                setActionState(ActionStates._RUNNING);
            }
            break;

            case _RUNNING:
            {
                //
                // When initialised, the ball has its direction set to STILL.
                // Therefore, set the initial direction.
                if (direction.isEmpty()
                    || !direction.hasXDirection()
                    || !direction.hasYDirection())
                {
                    direction.set
                        (
                            (MathUtils.random(100) < 50) ? Movement._DIRECTION_RIGHT : Movement._DIRECTION_LEFT,
                            Movement._DIRECTION_UP
                        );
                }

                speed.set(App.getEntities().getBallSpeed(), App.getEntities().getBallSpeed());

                sprite.translate
                    (
                        (speed.getX() * direction.getX()),
                        (speed.getY() * direction.getY())
                    );

                checkBounds();

                isRotating  = true;
                rotateSpeed = (4.0f * (direction.getX() * -1));
            }
            break;

            case _DYING:
            {
                Trace.fileInfo();

                if ((App.getEntities().balls.size - 1) <= 0)
                {
                    App.getGameProgress().lives.subtract(1);
                    App.getGameProgress().isRestarting = (App.getGameProgress().lives.getTotal() > 0);
                }

                setActionState(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.err("Invalid action state: ", getActionState());
            }
            break;
        }
    }

    /**
     * Toggle ball direction(s) if the ball is at the edges
     * of the screen.
     * The ball will die if Y < 0 unless GOD MODE is active,
     * in which case Y direction will toggle.
     */
    private void checkBounds()
    {
        if (direction.hasXDirection())
        {
            if (getPosition().x <= Gfx._LEFT_BOUNDARY)
            {
                direction.setX(Movement._DIRECTION_RIGHT);
                App.getAudio().startSound(AudioData.SFX_HIT);
            }

            if ((getPosition().x + frameWidth) >= Gfx._RIGHT_BOUNDARY)
            {
                direction.setX(Movement._DIRECTION_LEFT);
                App.getAudio().startSound(AudioData.SFX_HIT);
            }
        }

        if (direction.hasYDirection())
        {
            float checkPosition = (getPosition().y + frameHeight);

            if ((checkPosition >= (Gfx._VIEW_HEIGHT - 220))
                || ((checkPosition <= 0) && App.getAppConfig().isGodMode()))
            {
                direction.toggleY();
                App.getAudio().startSound(AudioData.SFX_HIT);
            }
            else
            {
                if (checkPosition <= 0)
                {
                    setActionState(ActionStates._DYING);
                }
            }
        }
    }

    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
        boolean hit = true;

        switch (cobjHitting.gid)
        {
//            case G_PADDLE:
//            {
//                bounce();
//
//                if (collisionObject.hasContactLeft())
//                {
//                    direction.setX(Movement._DIRECTION_RIGHT);
//                }
//                else if (collisionObject.hasContactRight())
//                {
//                    direction.setX(Movement._DIRECTION_LEFT);
//                }
//
//                if (collisionObject.hasContactUp())
//                {
//                    direction.setY(Movement._DIRECTION_DOWN);
//                }
//                else if (collisionObject.hasContactDown())
//                {
//                    direction.setY(Movement._DIRECTION_UP);
//                }
//            }
//            break;

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
                cobjHitting.parentEntity.setActionState(ActionStates._DYING);
                bounce();
            }
            break;

            case _WALL:
            case _GROUND:
            case _CEILING:
            case G_PADDLE:
            case G_BALL:
            {
                bounce();
            }
            break;

            default:
            {
                hit = false;
            }
            break;
        }

        if (hit)
        {
            App.getAudio().startSound(AudioData.SFX_HIT);
        }
    }

    @Override
    public void onNegativeCollision()
    {
    }

    private void bounce()
    {
        if (collisionObject.hasContactLeft() || collisionObject.hasContactRight())
        {
            direction.toggleX();
        }

        if (collisionObject.hasContactUp() || collisionObject.hasContactDown())
        {
            direction.toggleY();
        }
    }
}
