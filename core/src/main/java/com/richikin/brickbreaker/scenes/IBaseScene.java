package com.richikin.brickbreaker.scenes;

public interface IBaseScene
{
    void initialise();

    void update();

    void triggerFadeIn();

    void triggerFadeOut();

    void show();

    void hide();

    void render(float delta);

    void resize(int _width, int _height);

    void pause();

    void resume();

    void dispose();

    void loadImages();
}
