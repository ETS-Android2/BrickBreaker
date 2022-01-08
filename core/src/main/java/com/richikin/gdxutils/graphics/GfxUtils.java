package com.richikin.gdxutils.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GfxUtils
{
    public void splitRegion(TextureRegion srcRegion, int frameWidth, int frameHeight, TextureRegion[] destinationFrames)
    {
        TextureRegion[] tmpFrames = srcRegion.split(frameWidth, frameHeight)[0];
        System.arraycopy(tmpFrames, 0, destinationFrames, 0, tmpFrames.length);
    }

    public void drawRect(int x, int y, int width, int height, int thickness, Color color)
    {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        shapeRenderer.box(x, y, 0, width, height, thickness);
        shapeRenderer.end();
    }
}
