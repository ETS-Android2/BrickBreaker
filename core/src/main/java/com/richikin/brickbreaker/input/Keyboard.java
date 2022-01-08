package com.richikin.brickbreaker.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.richikin.brickbreaker.config.Settings;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.gdxutils.entities.IEntityComponent;
import com.richikin.gdxutils.input.DirectionMap;
import com.richikin.gdxutils.physics.Dir;
import com.richikin.gdxutils.physics.Direction;
import com.richikin.gdxutils.physics.DirectionValue;

public class Keyboard extends InputAdapter
{
    // =================================================================
    // DEFAULT Keyboard options.
    //
    public static final int defaultValueUp       = Input.Keys.W;
    public static final int defaultValueDown     = Input.Keys.S;
    public static final int defaultValueLeft     = Input.Keys.A;
    public static final int defaultValueRight    = Input.Keys.D;
    public static final int defaultValueA        = Input.Keys.NUMPAD_2;
    public static final int defaultValueB        = Input.Keys.NUMPAD_6;
    public static final int defaultValueX        = Input.Keys.NUMPAD_1;
    public static final int defaultValueY        = Input.Keys.NUMPAD_5;
    public static final int defaultValueHudInfo  = Input.Keys.F9;
    public static final int defaultValuePause    = Input.Keys.ESCAPE;
    public static final int defaultValueSettings = Input.Keys.F10;

    public boolean ctrlButtonHeld;
    public boolean shiftButtonHeld;

    private Vector2 lastTouchPoint;
    private boolean isTouchedDown;

    public Keyboard()
    {
        ctrlButtonHeld  = false;
        shiftButtonHeld = false;
        lastTouchPoint  = new Vector2();
        isTouchedDown   = false;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        boolean returnFlag = false;

        if (GdxSystem.inst().isDesktopApp())
        {
            if (App.getSettings().isEnabled(Settings._SCROLL_DEMO))
            {
//                App.getHud().buttonRight.press();
                returnFlag = true;
            }
            else
            {
                if (App.getAppConfig().gameScreenActive())
                {
                    returnFlag = maingameKeyDown(keycode);
                }
            }
        }

        return returnFlag;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        boolean returnFlag = false;

        if (GdxSystem.inst().isDesktopApp())
        {
            if (App.getAppConfig().gameScreenActive())
            {
                returnFlag = maingameKeyUp(keycode);
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyDown(int keycode)
    {
        boolean returnFlag;

        if (keycode == defaultValueLeft)
        {
            App.getHud().buttonLeft.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueRight)
        {
            App.getHud().buttonRight.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueUp)
        {
//            App.getHud().buttonUp.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueDown)
        {
//            App.getHud().buttonDown.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueB)
        {
//            App.getHud().buttonB.press();
            returnFlag = true;
        }
        else if (keycode == defaultValueA)
        {
//            App.getHud().buttonA.press();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.E:
                {
                    App.getGameProgress().lives.setTotal(1);
                    returnFlag = true;
                }
                break;

                case Input.Keys.MINUS:
                case Input.Keys.NUMPAD_SUBTRACT:
                {
                    for (IEntityComponent entity : App.getEntityData().getEntityMap())
                    {
                        if (entity.getType() == GraphicID.G_BRICK)
                        {
                            entity.setActionState(ActionStates._KILLED);
                        }
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    App.getHud().buttonPause.press();

                    returnFlag = true;
                }
                break;

                case Input.Keys.STAR:
                {
                    if (App.getAppConfig().isDevMode())
                    {
                        App.getBaseRenderer().resetCameraZoom();
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                {
                    shiftButtonHeld = true;
                    returnFlag      = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = true;
                    returnFlag     = true;
                }
                break;

                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyUp(int keycode)
    {
        boolean returnFlag;

        if (keycode == defaultValueLeft)
        {
            App.getHud().buttonLeft.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueRight)
        {
            App.getHud().buttonRight.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueUp)
        {
//            App.getHud().buttonUp.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueDown)
        {
//            App.getHud().buttonDown.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueB)
        {
//            App.getHud().buttonB.release();
            returnFlag = true;
        }
        else if (keycode == defaultValueA)
        {
//            App.getHud().buttonA.release();
            returnFlag = true;
        }
        else
        {
            switch (keycode)
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    App.getHud().buttonPause.release();

                    returnFlag = true;
                }
                break;

                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                {
                    shiftButtonHeld = false;
                    returnFlag      = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = false;
                    returnFlag     = true;
                }
                break;

                case Input.Keys.NUM_1:
                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = false;
                }
                break;
            }
        }

        return returnFlag;
    }

    @Override
    public boolean touchDown(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = App.getBaseRenderer().hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - App.getMapData().mapPosition.getX());
        int screenY = (int) (newPoints.y - App.getMapData().mapPosition.getY());

        lastTouchPoint.set(screenX, screenY);

        isTouchedDown = true;

        boolean returnFlag = false;

//        if (App.getAppConfig().gameScreenActive())
//        {
//            returnFlag = App.getInputManager().touchScreen.gameScreenTouchDown(screenX, screenY, pointer);
//        }
//        else
//        {
//            returnFlag = App.getInputManager().touchScreen.titleScreenTouchDown(screenX, screenY);
//        }

        return returnFlag;
    }

    @Override
    public boolean touchUp(int touchX, int touchY, int pointer, int button)
    {
        Vector2 newPoints = new Vector2(touchX, touchY);
        newPoints = App.getBaseRenderer().hudGameCamera.viewport.unproject(newPoints);

        int screenX = (int) (newPoints.x - App.getMapData().mapPosition.getX());
        int screenY = (int) (newPoints.y - App.getMapData().mapPosition.getY());

        isTouchedDown = false;

        boolean returnFlag;

        if (App.getAppConfig().gameScreenActive())
        {
            returnFlag = App.getInputManager().touchScreen.gameScreenTouchUp(screenX, screenY);
        }
        else
        {
            returnFlag = App.getInputManager().touchScreen.titleScreenTouchUp(screenX, screenY);
        }

        return returnFlag;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        Vector2 newPoints = new Vector2(screenX, screenY);
        newPoints = App.getBaseRenderer().mainGameCamera.viewport.unproject(newPoints);

        int touchX = (int) (newPoints.x - App.getMapData().mapPosition.getX());
        int touchY = (int) (newPoints.y - App.getMapData().mapPosition.getY());

        boolean returnFlag = false;

        if (App.getAppConfig().gameScreenActive())
        {
            Vector2 newTouch    = new Vector2(touchX, touchY);
            Vector2 delta       = newTouch.cpy().sub(lastTouchPoint);

            // delta.x > 0 means the touch moved to the right.
            // delta.x < 0 means the touch moved to the left.
            // delta.x == 0 means no movement.

            if (isTouchedDown
                && (lastTouchPoint.x < (Gfx._VIEW_WIDTH - App.getPlayer().frameWidth))
                && (lastTouchPoint.x > 0))
            {
                App.getPlayer().sprite.setPosition(touchX, App.getPlayer().sprite.getY());
            }

            lastTouchPoint.set(touchX, touchY);
        }

        return returnFlag;
    }

    /**
     * Process a movement of the mouse pointer.
     * Not called if any mouse button pressed.
     * Not called on iOS builds.
     *
     * @param screenX - new X coordinate.
     * @param screenY - new Y coordinate.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        Vector2 newPoints = new Vector2(screenX, screenY);
        newPoints = App.getBaseRenderer().hudGameCamera.viewport.unproject(newPoints);

        App.getInputManager().mouseWorldPosition.set(newPoints.x, newPoints.y);

        int touchX = (int) (newPoints.x - App.getMapData().mapPosition.getX());
        int touchY = (int) (newPoints.y - App.getMapData().mapPosition.getY());

        App.getInputManager().mousePosition.set(touchX, touchY);

        return false;
    }

    /**
     * React to the mouse wheel scrolling
     *
     * @param amountX - scroll amount.
     *                - amount < 0 == scroll left.
     *                - amount > 0 == scroll right.
     * @param amountY - scroll amount.
     *                - amount < 0 == scroll down.
     *                - amount > 0 == scroll up.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean scrolled(float amountX, float amountY)
    {
        if (App.getAppConfig().gameScreenActive())
        {
            if (App.getAppConfig().isDevMode())
            {
                if (ctrlButtonHeld)
                {
                    if (amountY < 0)
                    {
                        App.getBaseRenderer().gameZoom.out(0.10f);
                    }
                    else if (amountY > 0)
                    {
                        App.getBaseRenderer().gameZoom.in(0.10f);
                    }
                }

                if (shiftButtonHeld)
                {
                    if (amountY < 0)
                    {
                        App.getBaseRenderer().hudZoom.out(0.10f);
                    }
                    else if (amountY > 0)
                    {
                        App.getBaseRenderer().hudZoom.in(0.10f);
                    }
                }
            }
        }

        return false;
    }

    public Dir evaluateKeyboardDirection()
    {
        Direction direction = new Direction
            (
                (int) App.getInputManager().horizontalValue,
                (int) App.getInputManager().verticalValue
            );

        Dir keyDir = DirectionMap.map[DirectionMap.map.length - 1].translated;

        for (DirectionValue dv : DirectionMap.map)
        {
            if ((dv.dirX == direction.getX()) && (dv.dirY == direction.getY()))
            {
                keyDir = dv.translated;
            }
        }

        App.getInputManager().lastRegisteredDirection = keyDir;

        return keyDir;
    }

    public void translateXPercent()
    {
        App.getInputManager().horizontalValue = App.getPlayer().lookingAt.getX();
    }

    public void translateYPercent()
    {
        App.getInputManager().verticalValue = App.getPlayer().lookingAt.getY();
    }
}
