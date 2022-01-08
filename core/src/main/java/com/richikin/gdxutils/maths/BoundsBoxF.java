package com.richikin.gdxutils.maths;

public class BoundsBoxF
{
    public final float left;
    public final float right;
    public final float top;
    public final float bottom;

    public BoundsBoxF()
    {
        this.left   = 0;
        this.right  = 0;
        this.top    = 0;
        this.bottom = 0;
    }

    public BoundsBoxF(float left, float top, float right, float bottom)
    {
        this.left   = left;
        this.right  = right;
        this.top    = top;
        this.bottom = bottom;
    }

    @Override
    public String toString()
    {
        return "[" + left + ", " + top + "]" + "[" + right + ", " + bottom + "]";
    }
}
