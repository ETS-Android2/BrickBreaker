package com.richikin.brickbreaker.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.richikin.enumslib.GraphicID;

public class SpriteComponent implements Component
{
    public Sprite    sprite          = null;
    public boolean   isDrawable      = false;
    public GraphicID gid             = GraphicID.G_NO_ID;
    public GraphicID type            = GraphicID.G_NO_ID;
    public int       spriteNumber    = 0;
    public boolean   isMainCharacter = false;
}
