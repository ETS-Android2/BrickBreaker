package com.richikin.gdxutils.maths;

public class Vec3
{
    public int x;
    public int y;
    public int z;

    public Vec3()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vec3(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(int x, int y, int z)
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
