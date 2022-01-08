package com.richikin.gdxutils.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.maths.SimpleVec2F;
import com.richikin.gdxutils.maths.Vec2;
import com.richikin.gdxutils.maths.Vec2F;
import com.richikin.gdxutils.physics.Direction;

public interface IDefaultUIPanel
{
    void open();

    void close();

    void initialise(TextureRegion _region, String _nameID, Object... args);

    void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, SimpleVec2F speed);

    void setup();

    void draw(SpriteBatch spriteBatch);

    void activate();

    void deactivate();

    void populateTable();

    void setPosition(float x, float y);

    void forceZoomOut();

    void setPauseTime(int _time);

    boolean update();

    boolean getActiveState();

    boolean nameExists(String _nameID);

    Vec2 getSize();

    Vec2F getPosition();

    int getWidth();

    void setWidth(int _width);

    int getHeight();

    void setHeight(int _height);

    String getNameID();

    StateID getState();

    void setState(StateID _state);
}
