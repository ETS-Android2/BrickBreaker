package com.richikin.gdxutils.input;

public interface IGDXButton
{
    boolean checkPress(int touchX, int touchY);

    boolean checkRelease(int touchX, int touchY);

    void press();

    void pressConditional(boolean condition);

    boolean isPressed();

    boolean isDisabled();

    void setDisabled(boolean _state);

    boolean isDrawable();

    void setDrawable(boolean _state);

    void release();

    void toggleDisabled();

    void togglePressed();
}
