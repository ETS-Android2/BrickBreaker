package com.richikin.gdxutils.maths;

public class Vec3F
{
    public float x;
    public float y;
    public float z;

    public Vec3F()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vec3F(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y + ", z: " + z;
    }
}
