package com.richikin.gdxutils.graphics.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.graphics.GfxData;
import com.richikin.gdxutils.maths.SimpleVec3F;

public class StarObject implements Disposable
{
    private static final float _INITIAL_DEPTH       = 100.0f;
    private static final float _FINAL_DEPTH         = 1000.0f;
    private static final float _MINIMUM_VELOCITY    = 0.5f;
    private static final float _MAXIMUM_VELOCITY    = 5.0f;
    private static final float _MAXIMUM_STAR_RADIUS = 12.0f;

    private SimpleVec3F   position;
    private SimpleVec3F   velocity;
    private TextureRegion region;

    public StarObject()
    {
        position = new SimpleVec3F();
        velocity = new SimpleVec3F();
        region   = GdxApp.getAssets().getStarfieldObject();

        resetPosition();
    }

    public void update(float speed)
    {
        if ((position.z < 0) || (position.z > _FINAL_DEPTH)
            || (position.y < -GfxData._VIEW_HEIGHT) || (position.y > GfxData._VIEW_HEIGHT)
            || (position.x < -GfxData._VIEW_WIDTH) || (position.x > GfxData._VIEW_WIDTH))
        {
            resetPosition();
        }

        float deltaTime = Gdx.graphics.getDeltaTime();

        moveStar((velocity.x * speed) * deltaTime, (velocity.y * speed) * deltaTime, (velocity.z * speed) * deltaTime);
    }

    public void render(float speed)
    {
        update(speed);

        float x = (position.x / position.z) * (GfxData._VIEW_WIDTH * 0.5f);
        float y = (position.y / position.z) * (GfxData._VIEW_HEIGHT * 0.5f);

        float radius = ((_MAXIMUM_STAR_RADIUS - ((position.z * _MAXIMUM_STAR_RADIUS) * 0.0025f)) * velocity.z) * 0.2f;

        GdxApp.getSpriteBatch().draw(region, x, y, radius, radius);
    }

    private void resetPosition()
    {
        position.x = MathUtils.random(-GfxData._VIEW_WIDTH, GfxData._VIEW_WIDTH);
        position.y = MathUtils.random(-GfxData._VIEW_HEIGHT, GfxData._VIEW_HEIGHT);
        position.z = MathUtils.random(_INITIAL_DEPTH, _FINAL_DEPTH);
        velocity.z = MathUtils.random(_MINIMUM_VELOCITY, _MAXIMUM_VELOCITY);
    }

    private void moveStar(float x, float y, float z)
    {
        position.sub(x, y, z);
    }

    @Override
    public void dispose()
    {
        position = null;
        velocity = null;
        region   = null;
    }
}
