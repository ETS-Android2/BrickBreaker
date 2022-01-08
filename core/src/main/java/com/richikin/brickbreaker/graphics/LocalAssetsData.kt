package com.richikin.brickbreaker.graphics

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.richikin.enumslib.GraphicID
import com.richikin.gdxutils.assets.AssetLoader

/**
 * Provides link methods between GameCore and GdxUtils.
 * Candidate for removal when I've settled on a better way.
 */
class LocalAssetsData : AssetLoader()
{
    override fun getRedObjectName(): String
    {
        return "solid_red32x32"
    }

    override fun getBlueObjectName(): String
    {
        return "solid_blu32x32"
    }

    override fun getGreenObjectName(): String
    {
        return "solid_green32x32"
    }

    override fun getYellowObjectName(): String
    {
        return "solid_yellow32x32"
    }

    override fun getWhiteObjectName(): String
    {
        return "solid_white32x32"
    }

    override fun getSkinFilename(): String
    {
        return "data/uiskin.json"
    }

    override fun getDevPanelFont(): String
    {
        return GameAssets._PRO_WINDOWS_FONT
    }

    override fun getDevPanelBackground(): String
    {
        return GameAssets._BACKGROUND_ASSET
    }

    override fun getPausePanelBackground(): String
    {
        return GameAssets._PAUSE_PANEL_ASSET
    }

    override fun getOptionsPanelAsset(): String
    {
        return GameAssets._OPTIONS_PANEL_ASSET
    }

    override fun getControllerTestAsset(): String
    {
        return GameAssets._CONTROLLER_TEST_ASSET
    }

    override fun getStarfieldObject(): TextureRegion
    {
        return getObjectRegion("solid_white32x32")
    }

    override fun getPlayerGID(): GraphicID
    {
        return GraphicID.G_PADDLE
    }
}
