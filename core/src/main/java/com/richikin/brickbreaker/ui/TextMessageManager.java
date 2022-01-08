package com.richikin.brickbreaker.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.richikin.gdxutils.maths.Vec2;

import java.awt.*;

public class TextMessageManager
{
    private static class TextDetails
    {
        public String   message;
        public Color    color;
        public int      fontSize;
        public Vec2     position;

        public TextDetails(String msg, Color col, int size, Vec2 pos)
        {
            message     = msg;
            color       = col;
            fontSize    = size;
            position    = new Vec2(pos.x, pos.y);
        }
    }

    private BitmapFont font;

    public TextMessageManager()
    {
    }

    public void update()
    {
    }
}
