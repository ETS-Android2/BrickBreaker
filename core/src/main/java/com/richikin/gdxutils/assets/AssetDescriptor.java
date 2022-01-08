package com.richikin.gdxutils.assets;

public class AssetDescriptor
{
    public final String    assetName;
    public final int       frameCount;
    public final AssetSize assetSize;

    public AssetDescriptor(String _name, int _frames, AssetSize _size)
    {
        this.assetName  = _name;
        this.frameCount = _frames;
        this.assetSize  = _size;
    }
}
