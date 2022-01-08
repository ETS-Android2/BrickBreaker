package com.richikin.brickbreaker.entities.characters;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.core.GameProgress;
import com.richikin.brickbreaker.core.PointsManager;
import com.richikin.brickbreaker.entities.GdxSprite;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.physics.Movement;

public class Brick extends GdxSprite
{
    public Brick(GraphicID gid)
    {
        super(gid);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        bodyCategory = Gfx.CAT_FIXED_ENEMY;
        collidesWith = Gfx.CAT_PLAYER_WEAPON;

        create(descriptor, BodyDef.BodyType.StaticBody);

        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL);
        lookingAt.set(direction);

        isAnimating = true;

        sprite.setPosition(initXYZ.getX(), initXYZ.getY());
        speed.set(0, 0);

        collisionObject.clearCollision();

        //
        // Most levels have only stationary bricks.
        // Some will have bricks moving in set patterns.
        setActionState(App.getMapCreator().getBricksMoveType());

        if (getActionState() == ActionStates._LEFT_RIGHT)
        {
            direction.set
                (
                    MathUtils.randomBoolean() ? Movement._DIRECTION_LEFT : Movement._DIRECTION_RIGHT,
                    Movement._DIRECTION_STILL
                );
            speed.set(8.0f, 0.0f);
        }
    }

    @Override
    public void update()
    {
        switch (getActionState())
        {
            case _STANDING:
            {
            }
            break;

            case _LEFT_RIGHT:
            case _BOUNCE_AROUND:
            {
                moveBrickLeftRight();
            }
            break;

            case _DYING:
            case _KILLED:
            {
                if (getActionState() == ActionStates._DYING)
                {
                    PointsManager points = new PointsManager();

                    App.getGameProgress().stackPush
                        (
                            GameProgress.Stack._SCORE,
                            points.getPoints(gid)
                        );
                }

                setActionState(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.dbg("UNSUPPORTED ACTION STATE: ", getActionState());
            }
            break;
        }

        super.update();
    }

    @Override
    public void tidy(int index)
    {
        App.getGameProgress().bricksDestroyed++;

        App.getPickupManager().pickupPredet
            (
                (int) sprite.getX() + (frameWidth / 2),
                (int) sprite.getY()
            );

        collisionObject.kill();

        App.getEntityData().removeEntityAt(index);
    }

    private void moveBrickLeftRight()
    {
        if ((sprite.getX() <= Gfx._LEFT_BOUNDARY)
            || ((sprite.getX() + frameWidth) >= Gfx._RIGHT_BOUNDARY))
        {
            direction.toggleX();
        }

        sprite.translate((speed.getX() * direction.getX()), 0);
    }
}

