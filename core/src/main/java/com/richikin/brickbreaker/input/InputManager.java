package com.richikin.brickbreaker.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.physics.Dir;

public class InputManager implements Disposable
{
    public InputMultiplexer inputMultiplexer;
    public Vector2          mousePosition;
    public Vector2          mouseWorldPosition;
    public GameController   gameController;
    public Keyboard         keyboard;
    public TouchScreen      touchScreen;
    public Dir              currentRegisteredDirection;
    public Dir              lastRegisteredDirection;
    public float            horizontalValue;
    public float            verticalValue;

    public InputManager()
    {
    }

    public void setup()
    {
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(App.getStage());

        mousePosition      = new Vector2();
        mouseWorldPosition = new Vector2();
        keyboard           = new Keyboard();
        touchScreen        = new TouchScreen();

        inputMultiplexer.addProcessor(keyboard);

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
        Gdx.input.setCatchKey(Input.Keys.MENU, true);
        Gdx.input.setInputProcessor(inputMultiplexer);

        GdxSystem.inst().addBackButton("back_arrow", "back_arrow_pressed");
    }

    @Override
    public void dispose()
    {
        inputMultiplexer.clear();
        inputMultiplexer = null;

        mousePosition      = null;
        mouseWorldPosition = null;
        keyboard           = null;
        touchScreen        = null;
    }
}
