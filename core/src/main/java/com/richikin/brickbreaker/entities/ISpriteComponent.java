package com.richikin.brickbreaker.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.richikin.gdxutils.maths.SimpleVec3;
import com.richikin.gdxutils.physics.aabb.ICollisionCallback;

public interface ISpriteComponent
{
    void initialise(SpriteDescriptor descriptor);

    void create(SpriteDescriptor descriptor);

    void create(SpriteDescriptor descriptor, BodyDef.BodyType bodyType);

    void initPosition(SimpleVec3 vec3F);

    SimpleVec3 getPositionModifier();

    void preUpdate();

    void update();

    void postUpdate();

    void updateCommon();

    Vector3 getPosition();

    void preDraw();

    void draw(SpriteBatch spriteBatch);

    void animate();

    void setAnimation(SpriteDescriptor descriptor);

    void setAnimation(SpriteDescriptor descriptor, float frameRate);

    void setPositionFromBody();

    void addCollisionCallback(ICollisionCallback callback);

    void updateCollisionCheck();

    void updateCollisionBox();
}
