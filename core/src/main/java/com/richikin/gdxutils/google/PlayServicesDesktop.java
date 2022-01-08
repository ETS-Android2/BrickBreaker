
package com.richikin.gdxutils.google;

import com.richikin.gdxutils.logging.Trace;

public class PlayServicesDesktop implements IPlayServices
{
    @Override
    public void setup()
    {
    }

    @Override
    public void createApiClient()
    {
        Trace.dbg("Desktop: Services not enabled1");
    }

    @Override
    public void signIn()
    {
        Trace.dbg("Desktop: Services not enabled1");
    }

    @Override
    public void signInSilently()
    {
        Trace.dbg("Desktop: Services not enabled1");
    }

    @Override
    public void signOut()
    {
        Trace.dbg("Desktop: Services not enabled1");
    }

    @Override
    public boolean isSignedIn()
    {
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        return false;
    }

    @Override
    public void submitScore(int score, int level)
    {
        Trace.dbg("Desktop: Services not enabled1 : " + score + ", " + level);
    }

    @Override
    public void unlockAchievement(String achievementId)
    {
        Trace.dbg("Desktop: Services not enabled1 : " + achievementId);
    }

    @Override
    public void showAchievementScreen()
    {
        Trace.dbg("Desktop: Services not enabled1");
    }

    @Override
    public void showLeaderboard()
    {
        Trace.dbg("Desktop: Services not enabled1");
    }
}
