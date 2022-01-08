package com.richikin.gdxutils.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.richikin.gdxutils.maths.SimpleVec2F;
import com.richikin.gdxutils.maths.Vec2Boolean;

public class ScrollPaneObject
{
    public final Table       table;
    public final Skin        skin;
    public final SimpleVec2F size;
    public final SimpleVec2F position;
    public final Vec2Boolean fadeDisable;
    public final boolean     scrollFadeBars;
    public final String      name;

    public ScrollPaneObject()
    {
        table          = new Table();
        skin           = new Skin();
        size           = new SimpleVec2F();
        position       = new SimpleVec2F();
        fadeDisable    = new Vec2Boolean(false, false);
        scrollFadeBars = false;
        name           = "Undefined";
    }
}
