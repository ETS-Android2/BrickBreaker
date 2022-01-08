package com.richikin.gdxutils.input.controllers;

public class USBControllerPad implements ControlMapData
{
    public static final String _NAME = "USB Controller Pad";

    public static final float _MIN_RANGE          = -1.0f;
    public static final float _MAX_RANGE          = 1.0f;
    public static final float _DEAD_ZONE          = 0.5f;
    public static final int   _BUTTON_X           = 3;
    public static final int   _BUTTON_Y           = 0;
    public static final int   _BUTTON_A           = 2;
    public static final int   _BUTTON_B           = 1;
    public static final int   _BUTTON_BACK        = 8;
    public static final int   _BUTTON_START       = 9;
    public static final int   _BUTTON_LB          = 4;
    public static final int   _BUTTON_L3          = 8;
    public static final int   _BUTTON_RB          = 5;
    public static final int   _BUTTON_R3          = 9;
    public static final int   _AXIS_LEFT_X        = 1;    // -1 is left | +1 is right
    public static final int   _AXIS_LEFT_Y        = 0;    // -1 is up | +1 is down
    public static final int   _AXIS_LEFT_TRIGGER  = 10;   // value 0 to 1f
    public static final int   _AXIS_RIGHT_X       = 3;    // -1 is left | +1 is right
    public static final int   _AXIS_RIGHT_Y       = 2;    // -1 is up | +1 is down
    public static final int   _AXIS_RIGHT_TRIGGER = 11;   // value 0 to -1f
    public static final int   _DPAD_UP            = 11;
    public static final int   _DPAD_DOWN          = 12;
    public static final int   _DPAD_LEFT          = 13;
    public static final int   _DPAD_RIGHT         = 14;
    public static final int   _LEFT_TRIGGER       = 6;
    public static final int   _RIGHT_TRIGGER      = 7;
}
