
package com.richikin.gdxutils.google;

@SuppressWarnings({"SameReturnValue"})
public interface IAdsController
{
    void showBannerAd();

    void hideBannerAd();

    void showInterstitialAd(Runnable runnable);

    boolean isWifiConnected();
}
