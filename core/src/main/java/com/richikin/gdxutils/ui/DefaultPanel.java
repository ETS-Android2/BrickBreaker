package com.richikin.gdxutils.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.richikin.gdxutils.core.StateID;
import com.richikin.gdxutils.exceptions.NotImplementedException;
import com.richikin.gdxutils.graphics.SimpleDrawable;
import com.richikin.gdxutils.logging.StateManager;
import com.richikin.gdxutils.maths.SimpleVec2F;
import com.richikin.gdxutils.maths.Vec2;
import com.richikin.gdxutils.maths.Vec2F;
import com.richikin.gdxutils.physics.Direction;

/**
 * Basic UI Panel class.
 * All other panels should extend this class.
 */
public abstract class DefaultPanel implements IUserInterfacePanel
{
    protected TextureRegion  textureRegion;
    protected SimpleDrawable image;
    protected String         nameID;
    protected boolean        isActive;
    protected Table          table;
    protected Skin           skin;
    protected ScrollPane     scrollPane;

    private static final int _DEFAULT_X_SIZE = 16;
    private static final int _DEFAULT_Y_SIZE = 16;

    private final StateManager state;

    public DefaultPanel()
    {
        this.image        = new SimpleDrawable();
        this.image.size.x = _DEFAULT_X_SIZE;
        this.image.size.y = _DEFAULT_Y_SIZE;
        this.state        = new StateManager();
        this.isActive     = false;
        this.nameID       = "unnamed";
    }

    @Override
    public void open()
    {
        setup();
    }

    @Override
    public void close()
    {
        dispose();
    }

    @Override
    public boolean update()
    {
        return false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (isActive && (image != null))
        {
            image.draw(spriteBatch);
        }
    }

    @Override
    public boolean nameExists(String _nameID)
    {
        return _nameID.equals(this.nameID);
    }

    @Override
    public int getWidth()
    {
        return image.size.x;
    }

    @Override
    public void setWidth(int _width)
    {
        image.size.x = _width;
    }

    @Override
    public int getHeight()
    {
        return image.size.y;
    }

    @Override
    public void setHeight(int _height)
    {
        image.size.y = _height;
    }

    @Override
    public Vec2 getSize()
    {
        return image.size;
    }

    @Override
    public Vec2F getPosition()
    {
        return image.position;
    }

    @Override
    public void setPosition(float x, float y)
    {
        image.position.x = x;
        image.position.y = y;
    }

    @Override
    public String getNameID()
    {
        return nameID;
    }

    @Override
    public boolean getActiveState()
    {
        return isActive;
    }

    @Override
    public void activate()
    {
        isActive = true;
    }

    @Override
    public void deactivate()
    {
        isActive = false;
    }

    @Override
    public StateID getState()
    {
        return state.peek();
    }

    @Override
    public void setState(StateID _state)
    {
        state.set(_state);
    }


    // -------------------------------------------------------------------
    // Empty methods from IDefaultPanel interface
    @Override
    public void initialise(TextureRegion _region, String _nameID, Object... args)
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, SimpleVec2F speed)
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void setup()
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void populateTable()
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void setPauseTime(final int _time)
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    @Override
    public void forceZoomOut()
    {
        throw new NotImplementedException("ERROR - method not implemented!");
    }

    // -------------------------------------------------------------------
    // From Disposable Interface
    @Override
    public void dispose()
    {
    }
}
