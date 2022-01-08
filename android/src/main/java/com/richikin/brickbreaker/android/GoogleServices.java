
package com.richikin.brickbreaker.android;

import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.Task;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.config.StandardSettings;
import com.richikin.gdxutils.google.IPlayServices;
import com.richikin.gdxutils.google.RCConstants;
import com.richikin.gdxutils.logging.Trace;

public class GoogleServices implements IPlayServices
{
    private       GoogleSignInClient  googleSignInClient;
    private final AndroidLauncher     androidLauncher;
    private final AchievementsHandler achievementsHandler;
    private final LeaderboardHandler  leaderboardHandler;

    public GoogleServices(AndroidLauncher _androidLauncher)
    {
        this.androidLauncher     = _androidLauncher;
        this.achievementsHandler = new AchievementsHandler(androidLauncher);
        this.leaderboardHandler  = new LeaderboardHandler(androidLauncher);
    }

    @Override
    public void setup()
    {
    }

    /**
     * Creates the Google Sign In Client.
     */
    @Override
    public void createApiClient()
    {
        if (isEnabled())
        {
            // Configure sign-in options
            GoogleSignInOptions options = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Games.SCOPE_GAMES_LITE)
                .requestEmail()
                .build();

            // Build a sign-in client with the options specified by gso
            googleSignInClient = GoogleSignIn.getClient(androidLauncher, options);

            Trace.dbg("GPGS: GoogleSignInClient created.");

            checkAvailability();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        Trace.fileInfo();

        if (completedTask.isSuccessful())
        {
            Trace.dbg("GPGS: signInResult: PASS:");

            App.getSettings().getPrefs().putBoolean(StandardSettings._SIGN_IN_STATUS, true);
            App.getSettings().getPrefs().flush();
        }
        else
        {
            Trace.dbg("GPGS: signInResult: FAIL:");

            App.getSettings().getPrefs().putBoolean(StandardSettings._SIGN_IN_STATUS, false);
            App.getSettings().getPrefs().flush();
        }

        Trace.dbg("GPGS: " + GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(androidLauncher), Games.SCOPE_GAMES_LITE));
    }

    /**
     * Launches the sign in flow.
     * The result is returned in onActivityResult().
     */
    @Override
    public void signIn()
    {
        if (isEnabled())
        {
            Trace.fileInfo();

            androidLauncher.startActivityForResult
                (
                    googleSignInClient.getSignInIntent(),
                    RCConstants.RC_SIGN_IN.value
                );
        }
    }

    @Override
    public void signInSilently()
    {
        if (isEnabled())
        {
            Trace.fileInfo();

            if (googleSignInClient != null)
            {
                googleSignInClient.silentSignIn().addOnCompleteListener
                    (
                        androidLauncher, task ->
                        {
                            Trace.dbg("GPGS: signInResult: PASS:");

                            App.getSettings().getPrefs().putBoolean(StandardSettings._SIGN_IN_STATUS, true);
                            App.getSettings().getPrefs().flush();
                        });
            }
        }
    }

    @Override
    public void signOut()
    {
        if (isEnabled())
        {
            Trace.fileInfo();

            googleSignInClient.signOut().addOnCompleteListener
                (
                    androidLauncher, task -> Trace.dbg("GPGS: signOutResult: PASS")
                );
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return (GoogleSignIn.getLastSignedInAccount(androidLauncher) != null);
    }

    @Override
    public boolean isEnabled()
    {
        return GdxSystem.inst().isAndroidApp() || App.getSettings().getPrefs().getBoolean(StandardSettings._PLAY_SERVICES);
    }

    void onActivityResult(int requestCode, Intent data)
    {
        if (requestCode == RCConstants.RC_SIGN_IN.value)
        {
            Trace.dbg("GPGS: RC_SIGN_IN:");

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //====================================================================================

    /**
     * Unlock the specified achievement, if it has
     * not already unlocked.
     *
     * @param achievementId - String - The achievement ID.
     */
    @Override
    public void unlockAchievement(final String achievementId)
    {
        if (isEnabled())
        {
            achievementsHandler.unlockAchievement(achievementId);
        }
    }

    /**
     * Shows the achievements screen.
     */
    @Override
    public void showAchievementScreen()
    {
        if (isEnabled())
        {
            achievementsHandler.showAchievementScreen();
        }
    }

    /**
     * Submit score and level achieved.
     *
     * @param score - int - The score.
     * @param level - int - The level.
     */
    @Override
    public void submitScore(int score, int level)
    {
        if (isEnabled())
        {
            leaderboardHandler.submitScore(score, level);
        }
    }

    /**
     * Shows the global Leaderboard.
     */
    @Override
    public void showLeaderboard()
    {
        if (isEnabled())
        {
            leaderboardHandler.showLeaderBoard();
        }
    }

    // ===================================================================================

    private void checkAvailability()
    {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(androidLauncher) == ConnectionResult.SUCCESS)
        {
            Trace.divider();
            Trace.dbg("GPGS: Google Play Services ARE available");
            Trace.dbg("GPGS: API Version " + GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE);
        }
        else
        {
            Trace.divider();
            Trace.dbg("GPGS: Google Play Services ARE NOT available");
            Trace.dbg("GPGS: Google Play Services APK needs installing on this device...");
        }

        Trace.divider();
    }
}
