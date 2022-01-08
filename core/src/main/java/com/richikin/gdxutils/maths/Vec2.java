package com.richikin.gdxutils.maths;

public class Vec2
{
    public int x;
    public int y;

    public Vec2()
    {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y;
    }
}
