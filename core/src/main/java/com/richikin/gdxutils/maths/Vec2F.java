package com.richikin.gdxutils.maths;

public class Vec2F
{
    public float x;
    public float y;

    public Vec2F()
    {
        this.x = 0;
        this.y = 0;
    }

    public Vec2F(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y)
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
