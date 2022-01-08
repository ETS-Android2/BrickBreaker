package com.richikin.brickbreaker.physics;

import com.richikin.brickbreaker.entities.GdxSprite;
import com.richikin.enumslib.GraphicID;

public class BodyIdentity
{
    public final GraphicID gid;
    public final GraphicID type;
    public final GdxSprite entity;

    public BodyIdentity(GdxSprite entity, GraphicID gid, GraphicID type)
    {
        this.entity = entity;
        this.gid = gid;
        this.type = type;
    }
}
