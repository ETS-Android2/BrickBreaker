package com.richikin.brickbreaker.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.input.controllers.ControllerMap;
import com.richikin.gdxutils.input.controllers.ControllerPos;
import com.richikin.gdxutils.input.controllers.ControllerType;
import com.richikin.gdxutils.input.controllers.DefaultControllerMap;
import com.richikin.gdxutils.logging.Trace;

public class GameController implements ControllerListener
{
    public Controller controller;

    public GameController()
    {
    }

    public boolean setup()
    {
        GdxSystem.inst().controllersFitted = false;
        GdxSystem.inst().gameButtonsReady  = false;

        addExternalController();

        if (!GdxSystem.inst().controllersFitted)
        {
            if (Controllers.getControllers().size > 0)
            {
                Trace.dbg("Controllers.getControllers().size: " + Controllers.getControllers().size);

                for (Controller _controller : new Array.ArrayIterator<>(Controllers.getControllers()))
                {
                    Trace.dbg(_controller.getName());
                }

                Controllers.getControllers().clear();
            }
            else
            {
                Trace.dbg("No External Controller found");
            }

            Controllers.clearListeners();
        }

        return GdxSystem.inst().controllersFitted;
    }

    public void addExternalController()
    {
        try
        {
            if (Controllers.getControllers().size > 0)
            {
                controller = Controllers.getControllers().first();

                if (controller != null)
                {
                    Trace.dbg("Controller [" + controller.getName() + "] found");

                    GdxSystem.inst().controllersFitted = true;

                    createControllerMap();

                    Controllers.addListener(this);

                    Trace.dbg("Controller added");
                }
            }
            else
            {
                Trace.dbg("Controllers.getControllers().size: ", Controllers.getControllers().size);
            }
        }
        catch (NullPointerException npe)
        {
            Trace.dbg("::Failure");

            disableExternalControllers();

            if (GdxSystem.inst().isAndroidApp())
            {
                Trace.dbg("::Switched to _VIRTUAL");

                if (!GdxSystem.inst().availableInputs.contains(ControllerType._VIRTUAL, true))
                {
                    GdxSystem.inst().availableInputs.add(ControllerType._VIRTUAL);
                }
                GdxSystem.inst().virtualControllerPos = ControllerPos._LEFT;
            }
            else
            {
                Trace.dbg("::Switched to _KEYBOARD");

                if (!GdxSystem.inst().availableInputs.contains(ControllerType._KEYBOARD, true))
                {
                    GdxSystem.inst().availableInputs.add(ControllerType._KEYBOARD);
                }
                GdxSystem.inst().virtualControllerPos = ControllerPos._HIDDEN;
            }
        }
    }

    public void disableExternalControllers()
    {
        try
        {
            controller = null;

            GdxSystem.inst().controllersFitted = false;

            Controllers.removeListener(this);
        }
        catch (NullPointerException npe)
        {
            Trace.dbg("WARNING!: " + npe.getMessage());
        }
    }

    @Override
    public void connected(Controller controller)
    {
        GdxSystem.inst().controllersFitted = true;
    }

    @Override
    public void disconnected(Controller controller)
    {
        GdxSystem.inst().controllersFitted = false;
    }

    /**
     * A button on the {@link Controller} was pressed.
     * The buttonCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts button constants for known controllers.
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode)
    {
        return false;
    }

    /**
     * A button on the {@link Controller} was released.
     * The buttonCode is controller specific.
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts button constants for known controllers.
     */
    @Override
    public boolean buttonUp(Controller controller, int buttonCode)
    {
        return false;
    }

    /**
     * An axis on the {@link Controller} moved.
     * The axisCode is controller specific.
     * The axis value is in the range [-1, 1].
     * The <code>com.badlogic.gdx.controllers.mapping</code> package
     * hosts axes constants for known controllers.
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value)
    {
        return false;
    }

    private void createControllerMap()
    {
        Trace.fileInfo();

        ControllerMap._MIN_RANGE          = DefaultControllerMap._MIN_RANGE;
        ControllerMap._MAX_RANGE          = DefaultControllerMap._MAX_RANGE;
        ControllerMap._DEAD_ZONE          = DefaultControllerMap._DEAD_ZONE;
        ControllerMap._AXIS_LEFT_TRIGGER  = DefaultControllerMap._AXIS_LEFT_TRIGGER;
        ControllerMap._AXIS_RIGHT_TRIGGER = DefaultControllerMap._AXIS_RIGHT_TRIGGER;
        ControllerMap._AXIS_LEFT_X        = DefaultControllerMap._AXIS_LEFT_X;
        ControllerMap._AXIS_LEFT_Y        = DefaultControllerMap._AXIS_LEFT_Y;
        ControllerMap._AXIS_RIGHT_X       = DefaultControllerMap._AXIS_RIGHT_X;
        ControllerMap._AXIS_RIGHT_Y       = DefaultControllerMap._AXIS_RIGHT_Y;
        ControllerMap._BUTTON_A           = DefaultControllerMap._BUTTON_A;
        ControllerMap._BUTTON_B           = DefaultControllerMap._BUTTON_B;
        ControllerMap._BUTTON_X           = DefaultControllerMap._BUTTON_X;
        ControllerMap._BUTTON_Y           = DefaultControllerMap._BUTTON_Y;
        ControllerMap._BUTTON_START       = DefaultControllerMap._BUTTON_START;
        ControllerMap._BUTTON_BACK        = DefaultControllerMap._BUTTON_BACK;
        ControllerMap._BUTTON_L3          = DefaultControllerMap._BUTTON_L3;
        ControllerMap._BUTTON_R3          = DefaultControllerMap._BUTTON_R3;
        ControllerMap._BUTTON_LB          = DefaultControllerMap._BUTTON_LB;
        ControllerMap._BUTTON_RB          = DefaultControllerMap._BUTTON_RB;
        ControllerMap._DPAD_LEFT          = DefaultControllerMap._DPAD_LEFT;
        ControllerMap._DPAD_RIGHT         = DefaultControllerMap._DPAD_RIGHT;
        ControllerMap._DPAD_UP            = DefaultControllerMap._DPAD_UP;
        ControllerMap._DPAD_DOWN          = DefaultControllerMap._DPAD_DOWN;
//        ControllerMap._DPAD_CENTRE = DefaultControllerMap._DPAD_CENTRE;
        ControllerMap._LEFT_TRIGGER  = DefaultControllerMap._LEFT_TRIGGER;
        ControllerMap._RIGHT_TRIGGER = DefaultControllerMap._RIGHT_TRIGGER;
    }
}
