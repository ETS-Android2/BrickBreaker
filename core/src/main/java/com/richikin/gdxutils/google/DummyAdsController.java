
package com.richikin.gdxutils.google;

import com.richikin.gdxutils.logging.Trace;

public class DummyAdsController implements IAdsController
{
    @Override
    public void showBannerAd()
    {
        Trace.fileInfo();
    }

    @Override
    public void hideBannerAd()
    {
        Trace.fileInfo();
    }

    @Override
    public boolean isWifiConnected()
    {
        Trace.fileInfo();

        return false;
    }

    @Override
    public void showInterstitialAd(Runnable then)
    {
        Trace.fileInfo();

        if (then == null)
        {
            Trace.dbg("Runnable parameter is NULL");
        }
    }
}
