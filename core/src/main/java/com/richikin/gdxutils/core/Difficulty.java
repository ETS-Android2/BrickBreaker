package com.richikin.gdxutils.core;

public enum Difficulty
{
    //@formatter:off
    _EASY(  "EASY",     0, 0.5f),
    _NORMAL("NORMAL",   1, 1.0f),
    _HARD(  "HARD",     2, 2.0f);
    //@formatter:on

    private final String text;
    private final int    index;
    private final float  modifier;

    Difficulty(String _text, int _index, float _value)
    {
        text     = _text;
        index    = _index;
        modifier = _value;
    }

    public static Difficulty setFromIndex(float index)
    {
        if (index == _HARD.modifier)
        {
            return _HARD;
        }
        else if (index == _NORMAL.modifier)
        {
            return _NORMAL;
        }
        else
        {
            return _EASY;
        }
    }

    public String getText()
    {
        return text;
    }

    public float getModifier()
    {
        return modifier;
    }

    public int getIndex()
    {
        return index;
    }
}
