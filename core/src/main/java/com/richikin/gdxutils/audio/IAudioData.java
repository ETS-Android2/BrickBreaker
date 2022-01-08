package com.richikin.gdxutils.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public interface IAudioData
{
    void setup();

    void update();

    void playTune(boolean play);

    void playGameTune(boolean playTune);

    void playTitleTune(boolean playTune);

    void playHiScoreTune(boolean playTune);

    void startTune(int musicNumber, int volume, boolean looping);

    void startSound(int soundNumber);

    void tuneStop();

    void setMusicVolume(int volume);

    void setFXVolume(int volume);

    int getMusicVolume();

    int getFXVolume();

    int getDefaultMusicVolume();

    int getDefaultFXVolume();

    float getUsableFxVolume();

    void saveMusicVolume();

    void saveFXVolume();

    int getMusicVolumeSave();

    int getFXVolumeSave();

    Sound[] getSoundsTable();

    Music[] getMusicTable();

    boolean isTunePlaying();

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isTunePlaying(int tune);
}
