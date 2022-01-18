package com.richikin.brickbreaker.entities.characters;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.GdxSprite;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.physics.Movement;

public class Paddle extends GdxSprite
{
    private static final float _PLAYER_X_SPEED = 32;
    private static final float _PLAYER_Y_SPEED = 32;

    public boolean isStretched;
    public boolean isPossessed;
    public boolean isHurting;
    public boolean isMovingX;
    public boolean isMovingY;

    public Paddle()
    {
        super(GraphicID.G_PADDLE);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        Trace.fileInfo();

        bodyCategory = Gfx.CAT_PLAYER;
        collidesWith = Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_PICKUP;

        create(descriptor, BodyDef.BodyType.KinematicBody);

        setup(true);
    }

    public void setup(boolean isSpawning)
    {
        speed.set(_PLAYER_X_SPEED, _PLAYER_Y_SPEED);
        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL);
        lookingAt.set(direction);

        isStretched = false;
        isPossessed = false;
        isHurting   = false;
        isMovingX   = false;
        isMovingY   = false;
        isAnimating = true;
        isDrawable  = true;

        setExpanded(false);

        sprite.setRotation(0);
        sprite.setPosition(initXYZ.getX(), initXYZ.getY());
        updateCollisionBox();

        collisionObject.clearCollision();

        setActionState(ActionStates._STANDING);
    }

    @Override
    public void preUpdate()
    {
        if (getActionState() == ActionStates._RESTARTING)
        {
            sprite.setPosition(initXYZ.getX(), initXYZ.getY());
            setActionState(ActionStates._STANDING);
        }

        super.preUpdate();
    }

    @Override
    public void update()
    {
        if (App.getGameProgress().lives.isEmpty())
        {
            setActionState(ActionStates._DYING);
        } else
        {
            if (App.getAppState().peek() == StateID._STATE_PAUSED)
            {
                setActionState(ActionStates._PAUSED);
            }
        }

        updatePaddle();

        animate();

        updateCommon();
    }

    @Override
    public void tidy(int index)
    {
        collisionObject.kill();

        App.getEntityData().removeEntityAt(index);
    }

    @Override
    public void updateCollisionBox()
    {
        if (isStretched)
        {
            super.updateCollisionBox();
        } else
        {
            collisionObject.rectangle.x      = sprite.getX() + ((frameWidth / 3.0f) / 2);
            collisionObject.rectangle.y      = sprite.getY();
            collisionObject.rectangle.width  = ((frameWidth / 3.0f) * 2);
            collisionObject.rectangle.height = frameHeight;
        }
    }

    private void updatePaddle()
    {
        switch (getActionState())
        {
            case _RESETTING:
            case _RESTARTING:
            case _WAITING:
            case _DEAD:
            case _PAUSED:
            case _KILLED:
            {
                isDrawable = false;
            }
            break;

            case _SPAWNING:
            {
                isDrawable = true;

                setActionState(ActionStates._STANDING);
            }
            break;

            case _STANDING:
            {
                isDrawable = true;

                if (direction.hasDirection())
                {
                    lookingAt.set(direction);
                }

                checkButtons();
            }
            break;

            case _RIDING:
            case _RUNNING:
            {
                isDrawable = true;

                lookingAt.set(direction);

                checkButtons();

                movePaddle();
            }
            break;

            case _SET_DYING:
            case _DYING:
            {
                setActionState(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.err("Unsupported player action: ", getActionState());
            }
            break;
        }
    }

    public void movePaddle()
    {
        sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

        if (sprite.getX() < Gfx._LEFT_BOUNDARY)
        {
            sprite.setX(Gfx._LEFT_BOUNDARY);
        }

        if ((sprite.getX() + frameWidth) > Gfx._RIGHT_BOUNDARY)
        {
            sprite.setX(Gfx._RIGHT_BOUNDARY - frameWidth);
        }
    }

    public void setExpanded(boolean expanded)
    {
        SpriteDescriptor descriptor = App.getEntities().getDescriptor(GraphicID.G_PADDLE);

        if (expanded)
        {
            descriptor._ASSET = GameAssets._PADDLE_EXPANDED_ASSET;

            isStretched = true;
        } else
        {
            descriptor._ASSET = GameAssets._PADDLE_ASSET;

            isStretched = false;
        }

        descriptor._FRAMES = GameAssets._PADDLE_FRAMES;

        setAnimation(descriptor);
        updateCollisionBox();
    }

    private void checkButtons()
    {
        if (App.getHud().buttonLeft.isPressed())
        {
            if ((getPosition().x - speed.getX()) >= 0)
            {
                direction.setX(Movement._DIRECTION_LEFT);
                isMovingX = true;

                setActionState(ActionStates._RUNNING);
            } else
            {
                direction.setX(Movement._DIRECTION_STILL);
                isMovingX = false;

                setActionState(ActionStates._STANDING);
            }
        } else
        {
            if (App.getHud().buttonRight.isPressed())
            {
                if (((getPosition().x + frameWidth) + speed.getX()) < Gfx._VIEW_WIDTH)
                {
                    direction.setX(Movement._DIRECTION_RIGHT);
                    isMovingX = true;

                    setActionState(ActionStates._RUNNING);
                } else
                {
                    direction.setX(Movement._DIRECTION_STILL);
                    isMovingX = false;

                    setActionState(ActionStates._STANDING);
                }
            } else
            {
                direction.setX(Movement._DIRECTION_STILL);
                isMovingX = false;

                setActionState(ActionStates._STANDING);
            }
        }
    }
}
