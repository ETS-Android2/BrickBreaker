package com.richikin.gdxutils.maths;

public interface IItem
{
    void setToMaximum();

    void setToMinimum();

    boolean isFull();

    boolean isEmpty();

    boolean hasRoom();

    boolean isOverflowing();

    boolean isUnderflowing();

    void refill();
}
