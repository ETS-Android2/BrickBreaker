package com.richikin.gdxutils.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.gdxutils.core.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.gdxutils.physics.CollisionObject;

public interface IEntityComponent extends Disposable
{
    void setActionState(ActionStates action);

    ActionStates getActionState();

    CollisionObject getCollisionObject();

    void setCollisionObject(int xPos, int yPos);

    void setCollisionObject(float xPos, float yPos);

    Rectangle getCollisionRectangle();

    void tidy(int index);

    float getTopEdge();

    float getRightEdge();

    short getBodyCategory();

    short getCollidesWith();

    void setIndex(int index);

    int getIndex();

    GraphicID getGID();

    GraphicID getType();
}
