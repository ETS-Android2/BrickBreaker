package com.richikin.gdxutils.physics;

// TODO: 13/12/2021 - Rename
public class DirectionValue
{
    public final int dirX;
    public final int dirY;
    public final Dir translated;

    public DirectionValue(int x, int y, Dir trans)
    {
        dirX       = x;
        dirY       = y;
        translated = trans;
    }
}
