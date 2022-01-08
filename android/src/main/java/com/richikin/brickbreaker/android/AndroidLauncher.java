package com.richikin.brickbreaker.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.richikin.brickbreaker.android.GoogleServices;
import com.richikin.brickbreaker.core.MainGame;
import com.richikin.brickbreaker.graphics.Gfx;

public class AndroidLauncher extends AndroidApplication
{
	private GoogleServices googleServices;

    @SuppressLint("SourceLockedOrientationActivity")
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		config.useImmersiveMode = true;
		config.useWakelock      = true;
		config.hideStatusBar    = true;
		config.useAccelerometer = false;

		Gfx.setAndroidDimensions();

		// lock the current device orientation
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		googleServices = new GoogleServices(this);

		MainGame mainGame = new MainGame(googleServices);

		initialize(mainGame, config);

		Gdx.app.log("AndroidLauncher", "-------------------- APP START --------------------");
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		googleServices.onActivityResult(requestCode, data);
	}
}
