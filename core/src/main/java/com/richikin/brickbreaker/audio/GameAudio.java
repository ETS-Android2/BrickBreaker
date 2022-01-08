package com.richikin.brickbreaker.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.richikin.brickbreaker.config.Settings;
import com.richikin.brickbreaker.core.App;
import com.richikin.gdxutils.audio.IAudioData;
import com.richikin.gdxutils.config.GdxSystem;
import com.richikin.gdxutils.logging.Trace;

// TODO: 02/01/2022 - Work on moving the majority of this to GdxUtils.

public class GameAudio implements IAudioData
{
    private int     currentTune;
    private int     musicVolumeSave;
    private int     fxVolumeSave;
    private boolean soundsLoaded;
    private boolean musicLoaded;
    private boolean isTunePaused;

    public GameAudio()
    {
    }

    @Override
    public void setup()
    {
        soundsLoaded = false;
        musicLoaded  = false;
        isTunePaused = false;

        AudioData.sounds = new Sound[AudioData.MAX_SOUND];
        AudioData.music  = new Music[AudioData.MAX_TUNES];

        loadSounds();

        if (musicVolumeSave == 0)
        {
            musicVolumeSave = AudioData._DEFAULT_MUSIC_VOLUME;
        }

        if (fxVolumeSave == 0)
        {
            fxVolumeSave = AudioData._DEFAULT_FX_VOLUME;
        }
    }

    @Override
    public void update()
    {
        if (musicLoaded)
        {
            if (GdxSystem.inst().gamePaused)
            {
                if ((AudioData.music[currentTune] != null) && AudioData.music[currentTune].isPlaying())
                {
                    AudioData.music[currentTune].pause();
                    isTunePaused = true;
                }
            }
            else
            {
                if ((AudioData.music[currentTune] != null) && !AudioData.music[currentTune].isPlaying() && isTunePaused)
                {
                    AudioData.music[currentTune].play();
                    isTunePaused = false;
                }
            }
        }
    }

    private void loadSounds()
    {
        Trace.fileInfo();

        try
        {
            AudioData.sounds[AudioData.SFX_HIT]       = App.getAssets().loadSingleAsset("data/sounds/hit.wav", Sound.class);
            AudioData.sounds[AudioData.SFX_LOST]      = App.getAssets().loadSingleAsset("data/sounds/lost.wav", Sound.class);
            AudioData.sounds[AudioData.SFX_PICKUP]    = App.getAssets().loadSingleAsset("data/sounds/pickup.wav", Sound.class);
            AudioData.sounds[AudioData.SFX_EXTRALIFE] = App.getAssets().loadSingleAsset("data/sounds/extra_life.mp3", Sound.class);

            AudioData.music[AudioData.MUS_TITLE] = App.getAssets().loadSingleAsset("data/sounds/Bouncy.mp3", Music.class);
//            AudioData.music[AudioData.MUS_HISCORE] = App.getAssets().loadSingleAsset("data/sounds/breath.mp3", Music.class);
//            AudioData.music[AudioData.MUS_GAME]    = App.getAssets().loadSingleAsset("data/sounds/fear_mon.mp3", Music.class);

            App.getSettings().getPrefs().putInteger(Settings._MUSIC_VOLUME, AudioData._DEFAULT_MUSIC_VOLUME);
            App.getSettings().getPrefs().putInteger(Settings._FX_VOLUME, AudioData._DEFAULT_FX_VOLUME);
            App.getSettings().getPrefs().flush();

            soundsLoaded = AudioData.sounds.length > 0;
            musicLoaded  = AudioData.music.length > 0;
        }
        catch (Exception e)
        {
            Trace.err("SOUNDS NOT LOADED!");

            soundsLoaded = false;
            musicLoaded  = false;
        }
    }

    @Override
    public void playTune(boolean play)
    {
        if (currentTune >= 0)
        {
            if (play && musicLoaded)
            {
                startTune(currentTune, getMusicVolume(), true);
            }
            else
            {
                tuneStop();
            }
        }
    }

    /**
     * Play or Stop the Main Game tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    @Override
    public void playGameTune(boolean playTune)
    {
        if (AudioData.MUS_GAME >= 0)
        {
            if (playTune && musicLoaded)
            {
                startTune(AudioData.MUS_GAME, getMusicVolume(), true);
            }
            else
            {
                tuneStop();
            }
        }
    }

    /**
     * Play or Stop the Main Title tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    @Override
    public void playTitleTune(boolean playTune)
    {
        if (AudioData.MUS_TITLE >= 0)
        {
            if (playTune && musicLoaded)
            {
                startTune(AudioData.MUS_TITLE, getMusicVolume(), true);
            }
            else
            {
                tuneStop();
            }
        }
    }

    /**
     * Play or Stop the HiScore name entry tune.
     * This tune is played on the nname entry screen only,
     * NOT when the hiscore table is displayed in
     * the titles screen sequence.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    @Override
    public void playHiScoreTune(boolean playTune)
    {
        if (AudioData.MUS_HISCORE >= 0)
        {
            if (playTune & musicLoaded)
            {
                startTune(AudioData.MUS_HISCORE, getMusicVolume(), true);
            }
            else
            {
                tuneStop();
            }
        }
    }

    @Override
    public void startTune(int musicNumber, int volume, boolean looping)
    {
        if (musicLoaded && (musicNumber >= 0))
        {
            if (getMusicVolume() > 0)
            {
                if (App.getSettings().isEnabled(Settings._MUSIC_ENABLED)
                    && (AudioData.music != null)
                    && !AudioData.music[musicNumber].isPlaying())
                {
                    AudioData.music[musicNumber].setLooping(looping);
                    AudioData.music[musicNumber].setVolume(volume);
                    AudioData.music[musicNumber].play();

                    currentTune = musicNumber;
                }
            }
        }
    }

    @Override
    public void startSound(int soundNumber)
    {
        if (App.getSettings().isEnabled(Settings._SOUNDS_ENABLED) && soundsLoaded && (soundNumber >= 0))
        {
            if (getFXVolume() > 0)
            {
                if (AudioData.sounds[soundNumber] != null)
                {
                    AudioData.sounds[soundNumber].play(getFXVolume());
                }
            }
        }
    }

    @Override
    public void tuneStop()
    {
        if (musicLoaded && (currentTune >= 0))
        {
            if ((AudioData.music[currentTune] != null) && AudioData.music[currentTune].isPlaying())
            {
                AudioData.music[currentTune].stop();
            }
        }
    }

    @Override
    public void setMusicVolume(int volume)
    {
        if (musicLoaded && (currentTune >= 0))
        {
            if (AudioData.music[currentTune] != null)
            {
                AudioData.music[currentTune].setVolume(volume);
            }
        }

        App.getSettings().getPrefs().putInteger(Settings._MUSIC_VOLUME, volume);
        App.getSettings().getPrefs().flush();
    }

    @Override
    public void setFXVolume(int volume)
    {
        App.getSettings().getPrefs().putInteger(Settings._FX_VOLUME, volume);
        App.getSettings().getPrefs().flush();
    }

    @Override
    public int getMusicVolume()
    {
        return App.getSettings().getPrefs().getInteger(Settings._MUSIC_VOLUME);
    }

    @Override
    public int getFXVolume()
    {
        return App.getSettings().getPrefs().getInteger(Settings._FX_VOLUME);
    }

    @Override
    public int getDefaultMusicVolume()
    {
        return AudioData._DEFAULT_MUSIC_VOLUME;
    }

    @Override
    public int getDefaultFXVolume()
    {
        return AudioData._DEFAULT_FX_VOLUME;
    }

    @Override
    public float getUsableFxVolume()
    {
        return getFXVolume();
    }

    @Override
    public void saveMusicVolume()
    {
        musicVolumeSave = getMusicVolume();
    }

    @Override
    public void saveFXVolume()
    {
        fxVolumeSave = getFXVolume();
    }

    @Override
    public int getMusicVolumeSave()
    {
        return musicVolumeSave;
    }

    @Override
    public int getFXVolumeSave()
    {
        return fxVolumeSave;
    }

    @Override
    public Sound[] getSoundsTable()
    {
        return AudioData.sounds;
    }

    @Override
    public Music[] getMusicTable()
    {
        return AudioData.music;
    }

    @Override
    public boolean isTunePlaying()
    {
        return isTunePlaying(currentTune);
    }

    @Override
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isTunePlaying(int tune)
    {
        if (tune >= 0)
        {
            return musicLoaded && AudioData.music[tune].isPlaying();
        }

        return false;
    }
}
