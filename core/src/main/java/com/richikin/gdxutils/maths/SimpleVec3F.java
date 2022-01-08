package com.richikin.gdxutils.maths;

public class SimpleVec3F extends Vec3F
{
    public SimpleVec3F()
    {
        super();
    }

    public SimpleVec3F(float _x, float _y, float _z)
    {
        super(_x, _y, _z);
    }

    public SimpleVec3F(SimpleVec3F vec3)
    {
        super(vec3.getX(), vec3.getY(), vec3.getZ());
    }

    public void add(float x, float y, float z)
    {
        this.set(this.x + x, this.y + y, this.z + z);
    }

    public void addX(float value)
    {
        this.x += value;
    }

    public void addY(float value)
    {
        this.y += value;
    }

    public void sub(float x, float y, float z)
    {
        this.set(this.x - x, this.y - y, this.z - z);
    }

    public void subX(float value)
    {
        this.x -= value;
    }

    public void subY(float value)
    {
        this.y -= value;
    }

    public void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(SimpleVec3F vec2)
    {
        this.x = vec2.x;
        this.y = vec2.y;
        this.z = vec2.z;
    }

    public void mul(float mulX, float mulY, float mulZ)
    {
        this.x *= mulX;
        this.y *= mulY;
        this.z *= mulZ;
    }

    public boolean isEmpty()
    {
        return ((x == 0) && (y == 0) && (z == 0));
    }

    public void setEmpty()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public float getX()
    {
        return x;
    }

    public void setX(final float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(final float y)
    {
        this.y = y;
    }

    public float getZ()
    {
        return z;
    }

    public void setZ(final float z)
    {
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y + ", z: " + z;
    }
}
