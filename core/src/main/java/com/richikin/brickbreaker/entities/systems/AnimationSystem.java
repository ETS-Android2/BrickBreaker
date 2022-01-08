package com.richikin.brickbreaker.entities.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.entities.SpriteDescriptor;
import com.richikin.brickbreaker.entities.components.AnimationComponent;
import com.richikin.brickbreaker.entities.components.SpriteComponent;

public class AnimationSystem extends IteratingSystem
{
    private final ComponentMapper<SpriteComponent>    spriteMapper;
    private final ComponentMapper<AnimationComponent> animationMapper;

    public AnimationSystem()
    {
        super(Family.all(AnimationComponent.class, SpriteComponent.class).get());

        spriteMapper    = ComponentMapper.getFor(SpriteComponent.class);
        animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        if (animationMapper.get(entity).isAnimating)
        {
            AnimationComponent ac = animationMapper.get(entity);

            spriteMapper.get(entity).sprite.setRegion
                (
                    ac.animation.getKeyFrame(ac.elapsedAnimTime, ac.isLooping)
                );

            ac.elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }

    public void setAnimation(Entity entity, SpriteDescriptor descriptor)
    {
        AnimationComponent ac = animationMapper.get(entity);

        ac.animFrames = new TextureRegion[descriptor._FRAMES];

        TextureRegion asset = App.getAssets().getAnimationRegion(descriptor._ASSET);

        if (descriptor._SIZE != null)
        {
            ac.frameWidth  = descriptor._SIZE.x;
            ac.frameHeight = descriptor._SIZE.y;
        }
        else
        {
            ac.frameWidth  = asset.getRegionWidth() / descriptor._FRAMES;
            ac.frameHeight = asset.getRegionHeight();
        }

        TextureRegion[][] tmpFrames = asset.split(ac.frameWidth, ac.frameHeight);

        int i = 0;

        for (final TextureRegion[] tmpFrame : tmpFrames)
        {
            for (final TextureRegion textureRegion : tmpFrame)
            {
                if (i < descriptor._FRAMES)
                {
                    ac.animFrames[i++] = textureRegion;
                }
            }
        }

        ac.animation = new Animation<>(descriptor._ANIM_RATE / 6f, ac.animFrames);
        ac.animation.setPlayMode(descriptor._PLAYMODE);
    }
}
