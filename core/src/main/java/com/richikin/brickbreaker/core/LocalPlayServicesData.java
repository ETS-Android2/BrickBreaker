package com.richikin.brickbreaker.core;

import com.richikin.gdxutils.logging.Trace;

public class LocalPlayServicesData
{
    // -----------------------------------------------
    // The codes for achievements and leaderboards will be
    // given via Google play developers console when they
    // are submitted.
    //@formatter:off
    private final String[][] achievements =
        {
            {"achievement_douple_up",       "CgkI-9_N7dUbEAIQAA"},
            {"achievement_extra_life",      "CgkI-9_N7dUbEAIQAQ"},
            {"achievement_faster_faster",   "CgkI-9_N7dUbEAIQAg"},
            {"achievement_slow_poke",       "CgkI-9_N7dUbEAIQAw"},
            {"achievement_nothing_better",  "CgkI-9_N7dUbEAIQBA"},
            {"achievement_powerful_score",  "CgkI-9_N7dUbEAIQBQ"},
        };

    private final String[][] leaderboards =
        {
            {"leaderboard_leaderboard",         "CgkIvdearaEYEAIQAQ"},
            {"leaderboard_leaderboard_tester",  "CgkIvdearaEYEAIQAw"},
        };
    //@formatter:on

    private static final boolean achievementsEnabled = true;
    private static final boolean leaderboardEnabled = false;

    public LocalPlayServicesData()
    {
    }

    public void setup()
    {
        if (achievementsEnabled)
        {
            for (String[] achievement : achievements)
            {
                App.getPlayServicesData().addAchievementID(achievement[0], achievement[1]);
            }

            Trace.dbg("ACHIEVEMENTS Enabled.");
        }

        if (leaderboardEnabled)
        {
            for (String[] leaderboard : leaderboards)
            {
                App.getPlayServicesData().addLeaderBoardID(leaderboard[0], leaderboard[1]);
            }

            Trace.dbg("LEADERBOARD Enabled.");
        }
    }
}
