package com.richikin.brickbreaker.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component
{
    public Animation<TextureRegion> animation           = null;
    public float                    elapsedAnimTime     = 0;
    public TextureRegion[]          animFrames          = null;
    public boolean                  isAnimating         = false;
    public boolean                  isLooping           = false;
    public int                      frameWidth          = 0;
    public int                      frameHeight         = 0;
}
