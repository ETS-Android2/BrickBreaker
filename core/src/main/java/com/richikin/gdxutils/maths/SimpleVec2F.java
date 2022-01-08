package com.richikin.gdxutils.maths;

public class SimpleVec2F extends Vec2F
{
    public SimpleVec2F()
    {
        super();
    }

    public SimpleVec2F(float x, float y)
    {
        super(x, y);
    }

    public SimpleVec2F(SimpleVec2F vec2)
    {
        super(vec2.getX(), vec2.getY());
    }

    public void add(float x, float y)
    {
        this.set(this.x + x, this.y + y);
    }

    public void addX(float value)
    {
        this.x += value;
    }

    public void addY(float value)
    {
        this.y += value;
    }

    public void sub(float x, float y)
    {
        this.set(this.x - x, this.y - y);
    }

    public void subX(float value)
    {
        this.x -= value;
    }

    public void subY(float value)
    {
        this.y -= value;
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(SimpleVec2F vec2)
    {
        this.x = vec2.getX();
        this.y = vec2.getY();
    }

    public void mul(float mulX, float mulY)
    {
        this.x *= mulX;
        this.y *= mulY;
    }

    public boolean isEmpty()
    {
        return ((x == 0) && (y == 0));
    }

    public void setEmpty()
    {
        this.x = 0;
        this.y = 0;
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

    @Override
    public String toString()
    {
        return "x: " + x + ", y: " + y;
    }
}
