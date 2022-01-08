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
            // TODO: 07/01/2022
            {"achievement_base_destroyed",      "CgkIvdearaEYEAIQBA"},
            {"achievement_millionaire",         "CgkIvdearaEYEAIQBQ"},
            {"achievement_score_500k",          "CgkIvdearaEYEAIQBg"},
            {"achievement_score_100k",          "CgkIvdearaEYEAIQBw"},
            {"achievement_shoot_a_missile",     "CgkIvdearaEYEAIQCA"},
            {"achievement_courier_services",    "CgkIvdearaEYEAIQCQ"},
            {"achievement_bridge_building",     "CgkIvdearaEYEAIQDQ"},
            {"achievement_beam_me_up",          "CgkIvdearaEYEAIQCg"},
            {"achievement_gunman_jetman",       "CgkIvdearaEYEAIQCw"},
            {"achievement_bomb_collector",      "CgkIvdearaEYEAIQDA"},
            {"achievement_moon_rider",          "CgkIvdearaEYEAIQDg"},
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
