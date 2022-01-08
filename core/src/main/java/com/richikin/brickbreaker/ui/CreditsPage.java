package com.richikin.brickbreaker.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.richikin.brickbreaker.config.Version;
import com.richikin.brickbreaker.core.App;
import com.richikin.brickbreaker.graphics.GameAssets;
import com.richikin.brickbreaker.graphics.Gfx;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.core.GdxApp;
import com.richikin.gdxutils.logging.Trace;
import com.richikin.gdxutils.maths.Vec2;
import com.richikin.gdxutils.ui.Scene2DUtils;

public class CreditsPage implements IUIPage
{
    private Texture foreground;
    private Label   versionLabel;

    public CreditsPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.fileInfo();

        foreground = App.getAssets().loadSingleAsset(GameAssets._CREDITS_PANEL_ASSET, Texture.class);

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        versionLabel = scene2DUtils.addLabel
            (
                Version.getDisplayVersion(),
                new Vec2(80, (Gfx._HUD_HEIGHT - 740)),
                Color.WHITE,
                new Skin(Gdx.files.internal(GdxApp.getAssets().getSkinFilename()))
            );
        versionLabel.setFontScale(1.5f, 1.5f);
        versionLabel.setAlignment(Align.left);
    }

    @Override
    public boolean update()
    {
        return false;
    }

    @Override
    public void show()
    {
        Trace.fileInfo();

        if (GdxSystem.inst().backButton != null)
        {
            GdxSystem.inst().backButton.setVisible(true);
            GdxSystem.inst().backButton.setDisabled(false);
            GdxSystem.inst().backButton.setChecked(false);
        }

        if (versionLabel != null)
        {
            versionLabel.setVisible(true);
        }

        GdxSystem.inst().showAndEnableBackButton();
    }

    @Override
    public void hide()
    {
        Trace.fileInfo();

        if (GdxSystem.inst().backButton != null)
        {
            GdxSystem.inst().backButton.setVisible(false);
            GdxSystem.inst().backButton.setDisabled(true);
        }

        if (versionLabel != null)
        {
            versionLabel.setVisible(false);
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw
                (
                    foreground,
                    (App.getBaseRenderer().hudGameCamera.camera.viewportWidth - foreground.getWidth()) / 2,
                    (App.getBaseRenderer().hudGameCamera.camera.viewportHeight - foreground.getHeight()) / 2
                );
        }
    }

    @Override
    public void dispose()
    {
        Trace.fileInfo();

        App.getAssets().unloadAsset(GameAssets._CREDITS_PANEL_ASSET);

        if (versionLabel != null)
        {
            versionLabel.addAction(Actions.removeActor());
            versionLabel = null;
        }

        foreground = null;
    }
}
