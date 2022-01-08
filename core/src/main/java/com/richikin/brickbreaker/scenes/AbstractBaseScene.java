package com.richikin.brickbreaker.scenes;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.core.App;

public abstract class AbstractBaseScene extends ScreenAdapter implements IBaseScene, Disposable
{
    public AbstractBaseScene()
    {
        super();
    }

    @Override
    public void update()
    {
//        if (FadeEffect.isActive)
//        {
//            if (FadeEffect.update())
//            {
//                FadeEffect.end();
//            }
//        }
    }

    @Override
    public void triggerFadeIn()
    {
//        FadeEffect.triggerFadeIn();
    }

    @Override
    public void triggerFadeOut()
    {
//        FadeEffect.triggerFadeOut();
    }

    @Override
    public void resize(int _width, int _height)
    {
        App.getBaseRenderer().resizeCameras(_width, _height);
    }

    @Override
    public void pause()
    {
        App.getSettings().getPrefs().flush();
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void show()
    {
        loadImages();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void render(float delta)
    {
        App.getBaseRenderer().render();
    }

    @Override
    public void loadImages()
    {
    }

    @Override
    public void dispose()
    {
    }
}
