package com.richikin.brickbreaker.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component
{
    public final Vector3 position = new Vector3();
    public final Vector3 initXYZ  = new Vector3();

    public boolean isFlippedX = false;
    public boolean isFlippedY = false;
    public boolean canFlip    = false;

    public boolean isRotating  = false;
    public float   rotateSpeed = 0.0f;
    public float   rotation    = 0.0f;
}
