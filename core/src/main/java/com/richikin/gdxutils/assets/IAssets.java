package com.richikin.gdxutils.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.GraphicID;

public interface IAssets extends Disposable
{
    void initialise();

    <T> T loadSingleAsset(String asset, Class<?> type);

    void loadAtlas(String atlasName);

    void unloadAsset(String asset);

    TextureRegion getButtonRegion(String _name);

    TextureRegion getAnimationRegion(String _name);

    TextureRegion getObjectRegion(String _name);

    TextureRegion getTextRegion(String _name);

    TextureRegion getAchievementRegion(final String _name);

    AssetManager getAssetManager();

    TextureAtlas getButtonsLoader();

    TextureAtlas getAnimationsLoader();

    TextureAtlas getObjectsLoader();

    TextureAtlas getTextsLoader();

    TextureAtlas getAchievementsLoader();

    String getRedObjectName();

    String getBlueObjectName();

    String getGreenObjectName();

    String getYellowObjectName();

    String getWhiteObjectName();

    String getSkinFilename();

    String getDevPanelFont();

    String getDevPanelBackground();

    String getPausePanelBackground();

    String getOptionsPanelAsset();

    String getControllerTestAsset();

    TextureRegion getStarfieldObject();

    GraphicID getPlayerGID();
}
